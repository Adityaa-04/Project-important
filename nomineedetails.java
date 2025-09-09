import React, { use, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
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

const NomineeDetails = () => {
  const [formData, setFormData] = useState({
    nomineeName: "",
    nomineeDOB: "",
    nomineeRelationship: "",
    nomineeMobileNumber: "",
    nomineeEmailID: "",
    nomineePANNumber: "",
    nomineeAddress: "",
    attachments: {
      nomineeAadhaar: null,
      nomineeAddressProof: null,
    },
  });
  const [activeTab, setActiveTab] = useState("nomineeDetails"); // 'nomineeDetails' or 'attachments'

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

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Nominee Details Submitted:", formData);
    // In a real app, this would trigger navigation to the final verification page.
  };

  const renderNomineeDetails = () => (
    <section>
      <h2 className="text-xl font-semibold text-gray-800 mb-4 flex items-center gap-2">
        <Users className="w-5 h-5 text-sc-blue-600" />
        Nominee Information
      </h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label
            htmlFor="nomineeName"
            className="block text-sm font-medium text-gray-700"
          >
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
          <label
            htmlFor="nomineeDOB"
            className="block text-sm font-medium text-gray-700"
          >
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
          <label
            htmlFor="nomineeRelationship"
            className="block text-sm font-medium text-gray-700"
          >
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
          <label
            htmlFor="nomineeMobileNumber"
            className="block text-sm font-medium text-gray-700"
          >
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
          <label
            htmlFor="nomineeEmailID"
            className="block text-sm font-medium text-gray-700"
          >
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
          <label
            htmlFor="nomineePANNumber"
            className="block text-sm font-medium text-gray-700"
          >
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
      <div className="mt-4">
        <label
          htmlFor="nomineeAddress"
          className="block text-sm font-medium text-gray-700"
        >
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
      <p className="text-sm text-gray-600 mb-4">
        Please upload the nominee's documents.
      </p>

      <div className="space-y-4">
        {["nomineeAadhaar", "nomineeAddressProof"].map((docName, index) => (
          <div
            key={index}
            className="flex flex-col sm:flex-row items-start sm:items-center justify-between p-4 bg-gray-50 rounded-md border border-gray-200"
          >
            <label
              htmlFor={docName}
              className="block text-sm font-medium text-gray-700 sm:w-1/3"
            >
              {docName === "nomineeAadhaar"
                ? "Nominee Aadhaar"
                : "Nominee Address Proof"}
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
                <span className="text-xs text-gray-500 flex-shrink-0">
                  {formData.attachments[docName].name}
                </span>
              )}
            </div>
          </div>
        ))}
      </div>
    </section>
  );

  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-4 sm:p-6 font-sans">
      <BackgroundDecoration/>
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
          <h1 className="text-2xl sm:text-3xl font-bold text-gray-900 mb-2">
            Nominee Details
          </h1>
          <p className="text-sm text-gray-600 mb-6">
            Please provide the details of your loan nominee.
          </p>

          <div className="flex border-b border-gray-200 mb-8">
            <button
              type="button"
              onClick={() => setActiveTab("nomineeDetails")}
              className={`flex-1 py-3 px-1 text-center font-medium text-sm transition-colors duration-200 border-b-2 ${
                activeTab === "nomineeDetails"
                  ? "border-sc-blue-600 text-sc-blue-600"
                  : "border-transparent text-gray-500 hover:text-gray-700"
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
                activeTab === "attachments"
                  ? "border-sc-blue-600 text-sc-blue-600"
                  : "border-transparent text-gray-500 hover:text-gray-700"
              }`}
            >
              <span className="flex items-center justify-center gap-2">
                <FileText className="w-4 h-4" /> Attachments
              </span>
            </button>
          </div>

          <form onSubmit={handleSubmit} className="space-y-8">
            <AnimatePresence mode="wait">
              <motion.div
                key={activeTab}
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -10 }}
                transition={{ duration: 0.3 }}
                className="min-h-[600px]"
              >
                {activeTab === "nomineeDetails"
                  ? renderNomineeDetails()
                  : renderAttachments()}
              </motion.div>
            </AnimatePresence>

            <div className="flex justify-between mt-6">
              <button
                type="button"
                onClick={() => console.log("Navigate back to Loan Details")}
                className="inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-sc-blue-500 focus:ring-offset-2 transition-all duration-200"
              >
                <ArrowLeft className="h-4 w-4 mr-2" />
                Previous
              </button>
              <Link
                to="/verification"
                className="inline-flex items-center rounded-md border border-transparent bg-sc-blue-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-sc-blue-700 focus:outline-none focus:ring-2 focus:ring-sc-blue-500 focus:ring-offset-2 transition-all duration-200"
              >
                Next
                <ArrowRight className="h-4 w-4 ml-2" />
              </Link>
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
            <p>
              Feel free to ask me any questions you have about the process or
              the documents you need to submit.
            </p>
            <div className="p-4 bg-sc-blue-50 rounded-lg">
              <p className="text-sm">
                This is a placeholder for a live chatbot interface.
              </p>
              <p className="text-sm mt-2 text-sc-blue-800">
                Possible functionality: Answer FAQs, guide through form fields,
                check loan eligibility in real-time.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NomineeDetails;
