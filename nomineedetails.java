// src/pages/NomineeDetails.jsx
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { AnimatePresence, motion } from "framer-motion";
import ProgressBar from "./Components/ProgressBar";
import BackgroundDecoration from "./Components/BackgroundDecoration";
import {
  ArrowRight,
  ArrowLeft,
  Bot,
  FileText,
  Upload,
  Users,
} from "lucide-react";

const API_BASE = process.env.REACT_APP_API_URL || "http://localhost:8080";

const NomineeDetails = () => {
  const navigate = useNavigate();

  const [applicationId, setApplicationId] = useState(() =>
    localStorage.getItem("applicationId") || sessionStorage.getItem("applicationId") || ""
  );

  const [formData, setFormData] = useState({
    nomineeName: "",
    nomineeDOB: "",
    nomineeRelationship: "",
    nomineeMobileNumber: "",
    nomineeEmailID: "",
    nomineePANNumber: "",
    nomineeAadhaar: "",            // <-- added field for Aadhaar number (string)
    nomineeAddress: "",
    attachments: {
      nomineeAadhaar: null,
      nomineeAddressProof: null,
    },
  });

  const [activeTab, setActiveTab] = useState("nomineeDetails"); // 'nomineeDetails' or 'attachments'
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  // Fetch existing nominee (prefill) when component mounts / applicationId changes
  useEffect(() => {
    async function preload() {
      if (!applicationId) return;
      try {
        const token = localStorage.getItem("token");
        const res = await fetch(`${API_BASE}/api/applications/${applicationId}/nominee`, {
          headers: {
            "Content-Type": "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
          },
          credentials: "include",
        });
        if (!res.ok) {
          // If 404 => no nominee yet, that's fine
          if (res.status === 404) return;
          throw new Error(`Failed to preload nominee (${res.status})`);
        }
        const data = await res.json();
        setFormData((prev) => ({
          ...prev,
          nomineeName: data.nomineeName ?? "",
          nomineeDOB: data.nomineeDob ?? "",
          nomineeRelationship: data.relationship ?? "",
          nomineeMobileNumber: data.nomineePhone ?? "",
          nomineeEmailID: data.nomineeEmail ?? "",
          nomineePANNumber: data.nomineePan ?? "",
          nomineeAadhaar: data.nomineeAadhaar ?? "",
          nomineeAddress: data.nomineeAddress ?? "",
        }));
      } catch (err) {
        // don't spam user with preload errors; log to console for debugging
        console.error("Preload nominee error:", err);
      }
    }
    preload();
  }, [applicationId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleFileChange = (e, attachmentName) => {
    const file = e.target.files[0];
    setFormData((prevData) => ({
      ...prevData,
      attachments: {
        ...prevData.attachments,
        [attachmentName]: file,
      },
    }));
  };

  // Generic save handler. If goNext=true then navigate('/verification') on success.
  const handleSubmit = async (e, goNext = false) => {
    if (e && e.preventDefault) e.preventDefault();
    setError("");
    setSuccessMessage("");

    if (!applicationId) {
      setError("Missing applicationId. Set it in the Create Application step or in localStorage.");
      return;
    }

    // build payload matching backend fields
    const payload = {
      nomineeName: formData.nomineeName || null,
      relationship: formData.nomineeRelationship || null,
      nomineeDob: formData.nomineeDOB || null,            // yyyy-mm-dd from <input type="date">
      nomineeAddress: formData.nomineeAddress || null,
      nomineePhone: formData.nomineeMobileNumber || null,
      nomineeEmail: formData.nomineeEmailID || null,
      nomineeAadhaar: formData.nomineeAadhaar || null,    // user-entered Aadhaar number (not file)
      nomineePan: formData.nomineePANNumber || null,
    };

    try {
      setLoading(true);
      const token = localStorage.getItem("token");
      const res = await fetch(`${API_BASE}/api/applications/${applicationId}/nominee`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        credentials: "include",
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || `Failed to save nominee (${res.status})`);
      }

      const saved = await res.json();
      setSuccessMessage("Nominee saved successfully 🎉");
      // update local form with returned values (in case backend filled/normalized anything)
      setFormData((prev) => ({
        ...prev,
        nomineeName: saved.nomineeName ?? prev.nomineeName,
        nomineeDOB: saved.nomineeDob ?? prev.nomineeDOB,
        nomineeRelationship: saved.relationship ?? prev.nomineeRelationship,
        nomineeMobileNumber: saved.nomineePhone ?? prev.nomineeMobileNumber,
        nomineeEmailID: saved.nomineeEmail ?? prev.nomineeEmailID,
        nomineePANNumber: saved.nomineePan ?? prev.nomineePANNumber,
        nomineeAadhaar: saved.nomineeAadhaar ?? prev.nomineeAadhaar,
        nomineeAddress: saved.nomineeAddress ?? prev.nomineeAddress,
      }));

      if (goNext) {
        // small delay to allow user to see success message before nav (optional)
        setTimeout(() => navigate("/verification"), 400);
      }
      return true;
    } catch (err) {
      console.error("Save nominee error:", err);
      setError(err.message || "Failed to save nominee");
      return false;
    } finally {
      setLoading(false);
    }
  };

  const renderNomineeDetails = () => (
    <section>
      <h2 className="text-xl font-semibold text-gray-800 mb-4 flex items-center gap-2">
        <Users className="w-5 h-5 text-sc-blue-600" />
        Nominee Information
      </h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label htmlFor="nomineeName" className="block text-sm font-medium text-gray-700">
            Nominee Name
          </label>
          <input
            type="text"
            id="nomineeName"
            name="nomineeName"
            placeholder="Nominee Name"
            value={formData.nomineeName}
            onChange={handleInputChange}
            className="mt-1 w-full p-3 rounded-md border border-gray-300 shadow-sm focus:border-sc-blue-500 focus:ring-sc-blue-500 sm:text-sm"
          />
        </div>
        <div>
          <label htmlFor="nomineeDOB" className="block text-sm font-medium text-gray-700">
            Nominee DOB
          </label>
          <input
            type="date"
            id="nomineeDOB"
            name="nomineeDOB"
            value={formData.nomineeDOB}
            onChange={handleInputChange}
            className="mt-1 w-full p-3 rounded-md border border-gray-300 shadow-sm focus:border-sc-blue-500 focus:ring-sc-blue-500 sm:text-sm"
          />
        </div>
      </div>
      <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label htmlFor="nomineeRelationship" className="block text-sm font-medium text-gray-700">
            Relationship to Applicant
          </label>
          <input
            type="text"
            id="nomineeRelationship"
            name="nomineeRelationship"
            placeholder="e.g., Spouse, Parent"
            value={formData.nomineeRelationship}
            onChange={handleInputChange}
            className="mt-1 w-full p-3 rounded-md border border-gray-300 shadow-sm focus:border-sc-blue-500 focus:ring-sc-blue-500 sm:text-sm"
          />
        </div>
        <div>
          <label htmlFor="nomineeMobileNumber" className="block text-sm font-medium text-gray-700">
            Nominee Mobile Number
          </label>
          <input
            type="tel"
            id="nomineeMobileNumber"
            name="nomineeMobileNumber"
            placeholder="e.g., (123) 456-7890"
            value={formData.nomineeMobileNumber}
            onChange={handleInputChange}
            className="mt-1 w-full p-3 rounded-md border border-gray-300 shadow-sm focus:border-sc-blue-500 focus:ring-sc-blue-500 sm:text-sm"
          />
        </div>
      </div>
      <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label htmlFor="nomineeEmailID" className="block text-sm font-medium text-gray-700">
            Nominee Email ID
          </label>
          <input
            type="email"
            id="nomineeEmailID"
            name="nomineeEmailID"
            placeholder="e.g., nominee@example.com"
            value={formData.nomineeEmailID}
            onChange={handleInputChange}
            className="mt-1 w-full p-3 rounded-md border border-gray-300 shadow-sm focus:border-sc-blue-500 focus:ring-sc-blue-500 sm:text-sm"
          />
        </div>
        <div>
          <label htmlFor="nomineePANNumber" className="block text-sm font-medium text-gray-700">
            Nominee PAN Number
          </label>
          <input
            type="text"
            id="nomineePANNumber"
            name="nomineePANNumber"
            placeholder="e.g., ABCDE1234F"
            value={formData.nomineePANNumber}
            onChange={handleInputChange}
            className="mt-1 w-full p-3 rounded-md border border-gray-300 shadow-sm focus:border-sc-blue-500 focus:ring-sc-blue-500 sm:text-sm"
          />
        </div>
      </div>

      <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label htmlFor="nomineeAadhaar" className="block text-sm font-medium text-gray-700">
            Nominee Aadhaar (number)
          </label>
          <input
            type="text"
            id="nomineeAadhaar"
            name="nomineeAadhaar"
            placeholder="12-digit Aadhaar number"
            value={formData.nomineeAadhaar}
            onChange={handleInputChange}
            maxLength={12}
            className="mt-1 w-full p-3 rounded-md border border-gray-300 shadow-sm focus:border-sc-blue-500 focus:ring-sc-blue-500 sm:text-sm"
          />
        </div>
      </div>

      <div className="mt-4">
        <label htmlFor="nomineeAddress" className="block text-sm font-medium text-gray-700">
          Nominee Address
        </label>
        <textarea
          id="nomineeAddress"
          name="nomineeAddress"
          placeholder="Nominee Address"
          rows="3"
          value={formData.nomineeAddress}
          onChange={handleInputChange}
          className="mt-1 w-full p-3 rounded-md border border-gray-300 shadow-sm focus:border-sc-blue-500 focus:ring-sc-blue-500 sm:text-sm"
        ></textarea>
      </div>
    </section>
  );

  const renderAttachments = () => (
    <section>
      <h2 className="text-xl font-semibold text-gray-800 mb-4 flex items-center gap-2">
        <Upload className="w-5 h-5 text-sc-blue-600" />
        Required Attachments
      </h2>
      <p className="text-sm text-gray-600 mb-4">Please upload the nominee's documents.</p>

      <div className="space-y-4">
        {["nomineeAadhaar", "nomineeAddressProof"].map((docName, index) => (
          <div
            key={index}
            className="flex flex-col sm:flex-row items-start sm:items-center justify-between p-4 bg-gray-50 rounded-md border border-gray-200"
          >
            <label htmlFor={docName} className="block text-sm font-medium text-gray-700 sm:w-1/3">
              {docName === "nomineeAadhaar" ? "Nominee Aadhaar" : "Nominee Address Proof"}
            </label>
            <div className="mt-2 sm:mt-0 sm:w-2/3 flex items-center gap-2">
              <input
                type="file"
                id={docName}
                name={docName}
                onChange={(e) => handleFileChange(e, docName)}
                className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-sc-blue-50 file:text-sc-blue-700 hover:file:bg-sc-blue-100"
              />
              {formData.attachments[docName] && (
                <span className="text-xs text-gray-500 flex-shrink-0">{formData.attachments[docName].name}</span>
              )}
            </div>
          </div>
        ))}
      </div>
    </section>
  );

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-4 sm:p-6 font-sans">
      <BackgroundDecoration />
      <ProgressBar currentStep={4} />
      <button
        onClick={() => navigate(-1)}
        className="fixed top-4 left-4 z-50 flex items-center gap-2 px-4 py-2 rounded-full bg-white shadow-lg text-gray-700 hover:bg-gray-200 transition-colors transform hover:scale-105"
      >
        <ArrowLeft className="w-5 h-5" />
        Back
      </button>
      <div className="flex flex-col max-w-[88rem] md:flex-row w-full gap-8 items-start">
        {/* Main form container */}
        <div className="w-full md:w-3/4 bg-white rounded-2xl shadow-2xl overflow-hidden p-6 sm:p-10">
          <h1 className="text-2xl sm:text-3xl font-bold text-gray-900 mb-2">Nominee Details</h1>
          <p className="text-sm text-gray-600 mb-6">Please provide the details of your loan nominee.</p>

          <div className="flex border-b border-gray-200 mb-8">
            <button
              type="button"
              onClick={() => setActiveTab("nomineeDetails")}
              className={`flex-1 py-3 px-1 text-center font-medium text-sm transition-colors duration-200 border-b-2 ${
                activeTab === "nomineeDetails" ? "border-sc-blue-600 text-sc-blue-600" : "border-transparent text-gray-500 hover:text-gray-700"
              }`}
            >
              <span className="flex items-center justify-center gap-2">
                <Users className="w-4 h-4" /> Nominee Details
              </span>
            </button>
            <button
              type="button"
              onClick={() => setActiveTab("attachments")}
              className={`flex-1 py-3 px-1 text-center font-medium text-sm transition-colors duration-200 border-b-2 ${
                activeTab === "attachments" ? "border-sc-blue-600 text-sc-blue-600" : "border-transparent text-gray-500 hover:text-gray-700"
              }`}
            >
              <span className="flex items-center justify-center gap-2">
                <FileText className="w-4 h-4" /> Attachments
              </span>
            </button>
          </div>

          <form onSubmit={(e) => handleSubmit(e, false)} className="space-y-8">
            {error && <div className="p-3 bg-red-100 text-red-700 rounded mb-3">{error}</div>}
            {successMessage && <div className="p-3 bg-green-100 text-green-700 rounded mb-3">{successMessage}</div>}
            <AnimatePresence mode="wait">
              <motion.div key={activeTab} initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -10 }} transition={{ duration: 0.3 }} className="min-h-[600px]">
                {activeTab === "nomineeDetails" ? renderNomineeDetails() : renderAttachments()}
              </motion.div>
            </AnimatePresence>

            <div className="flex justify-between mt-6">
              <button
                type="button"
                onClick={() => navigate(-1)}
                className="inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-sc-blue-500 focus:ring-offset-2 transition-all duration-200"
              >
                <ArrowLeft className="h-4 w-4 mr-2" />
                Previous
              </button>

              <div className="flex gap-3">
                <button
                  type="submit"
                  disabled={loading}
                  className="inline-flex items-center rounded-md border border-transparent bg-sc-blue-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-sc-blue-700 focus:outline-none focus:ring-2 focus:ring-sc-blue-500 focus:ring-offset-2 transition-all duration-200"
                >
                  {loading ? "Saving..." : "Save Nominee"}
                </button>

                <button
                  type="button"
                  onClick={() => handleSubmit(null, true)}
                  className="inline-flex items-center rounded-md border border-transparent bg-sc-blue-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-sc-blue-700 focus:outline-none focus:ring-2 focus:ring-sc-blue-500 focus:ring-offset-2 transition-all duration-200"
                >
                  Next
                  <ArrowRight className="h-4 w-4 ml-2" />
                </button>
              </div>
            </div>
          </form>
        </div>

        {/* Floating Chatbot Companion */}
        <div className="hidden md:block md:w-2/5 h-[80vh] bg-white rounded-2xl shadow-2xl p-6 border border-gray-200 overflow-y-auto sticky top-4">
          <div className="flex items-center gap-3 text-sc-blue-600 mb-4 border-b pb-4">
            <Bot className="w-6 h-6" />
            <h3 className="text-lg font-bold">Your Loan Companion</h3>
          </div>
          <div className="text-gray-700 space-y-4">
            <p>Hi there! I'm here to help you with your loan application.</p>
            <p>Feel free to ask me any questions you have about the process or the documents you need to submit.</p>
            <div className="p-4 bg-sc-blue-50 rounded-lg">
              <p className="text-sm">This is a placeholder for a live chatbot interface.</p>
              <p className="text-sm mt-2 text-sc-blue-800">Possible functionality: Answer FAQs, guide through form fields, check loan eligibility in real-time.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NomineeDetails;
