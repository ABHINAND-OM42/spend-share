import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const CreateGroupPage = () => {
  const navigate = useNavigate();
  const currentUser = JSON.parse(localStorage.getItem("currentUser"));

  // --- STATE ---
  const [formData, setFormData] = useState({
    name: "",
    description: ""
  });
  
  const [availableUsers, setAvailableUsers] = useState([]);
  const [selectedFriendIds, setSelectedFriendIds] = useState([]);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(true);

  // --- NEW: SEARCH STATE ---
  const [searchTerm, setSearchTerm] = useState("");

  // --- FETCH USERS ON LOAD ---
  useEffect(() => {
    if (!currentUser) {
      navigate("/login");
      return;
    }
    fetchUsers();
  }, [navigate]);

  const fetchUsers = async () => {
    setIsLoading(true);
    try {
      const response = await axios.get("http://localhost:8080/api/users");
      const usersList = response.data.data ? response.data.data : response.data;
      
      if (Array.isArray(usersList)) {
        const others = usersList.filter(u => u.id !== currentUser.id);
        setAvailableUsers(others);
      }
    } catch (err) {
      console.error("Failed to load users", err);
      setError("Could not load friends list.");
    } finally {
      setIsLoading(false);
    }
  };

  // --- HANDLERS ---
  const handleInputChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleCheckboxChange = (userId) => {
    if (selectedFriendIds.includes(userId)) {
      setSelectedFriendIds(selectedFriendIds.filter(id => id !== userId));
    } else {
      setSelectedFriendIds([...selectedFriendIds, userId]);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!formData.name.trim()) {
      setError("Group name is required");
      return;
    }

    try {
      const payload = {
        name: formData.name,
        description: formData.description,
        memberIds: [currentUser.id, ...selectedFriendIds]
      };

      await axios.post("http://localhost:8080/api/groups", payload);
      alert("Group Created Successfully!");
      navigate("/dashboard"); 

    } catch (err) {
      console.error("Creation failed", err);
      setError("Failed to create group. Please try again.");
    }
  };

  // --- NEW: FILTER LOGIC ---
  // We check if the name OR the email matches the search text (case insensitive)
  const filteredUsers = availableUsers.filter(user => 
    user.name.toLowerCase().includes(searchTerm.toLowerCase()) || 
    user.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // --- STYLES ---
  const styles = {
    container: { padding: "40px", maxWidth: "600px", margin: "0 auto", fontFamily: "'Segoe UI', sans-serif" },
    header: { marginBottom: "30px", borderBottom: "1px solid #eee", paddingBottom: "10px" },
    formGroup: { marginBottom: "20px" },
    label: { display: "block", marginBottom: "8px", fontWeight: "bold", color: "#333" },
    input: { width: "100%", padding: "12px", border: "1px solid #ddd", borderRadius: "4px", fontSize: "16px", boxSizing: "border-box" },
    
    // Search specific style
    searchInput: { 
        width: "100%", 
        padding: "8px 12px", 
        marginBottom: "10px", 
        border: "1px solid #ccc", 
        borderRadius: "4px", 
        fontSize: "14px",
        backgroundColor: "#f9f9f9"
    },

    friendsBox: { border: "1px solid #ddd", borderRadius: "4px", height: "250px", overflowY: "auto", padding: "10px", backgroundColor: "#fff" },
    friendItem: { display: "flex", alignItems: "center", padding: "10px 0", borderBottom: "1px solid #f0f0f0" },
    checkbox: { marginRight: "12px", transform: "scale(1.2)", cursor: "pointer" },
    btnContainer: { display: "flex", gap: "10px", marginTop: "30px" },
    submitBtn: { padding: "12px 24px", backgroundColor: "#5bc5a7", color: "white", border: "none", borderRadius: "4px", cursor: "pointer", fontSize: "16px", fontWeight: "bold" },
    cancelBtn: { padding: "12px 24px", backgroundColor: "#eee", color: "#333", border: "none", borderRadius: "4px", cursor: "pointer", fontSize: "16px" },
    error: { color: "red", marginBottom: "15px", padding: "10px", backgroundColor: "#ffebee", borderRadius: "4px" },
    
    // Style for when search finds nothing
    emptySearch: { textAlign: "center", color: "#999", fontStyle: "italic", marginTop: "20px" }
  };

  return (
    <div style={styles.container}>
      <div style={styles.header}>
        <h2>Start a new group</h2>
      </div>

      {error && <div style={styles.error}>{error}</div>}

      <form onSubmit={handleSubmit}>
        <div style={styles.formGroup}>
          <label style={styles.label}>My group shall be called...</label>
          <input 
            style={styles.input}
            type="text"
            name="name"
            placeholder="e.g. Summer Trip, Office Lunch"
            value={formData.name}
            onChange={handleInputChange}
            autoFocus
          />
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Description (Optional)</label>
          <input 
            style={styles.input}
            type="text"
            name="description"
            placeholder="What's this group for?"
            value={formData.description}
            onChange={handleInputChange}
          />
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Add Group Members</label>
          
          {/* --- NEW SEARCH BAR --- */}
          <input 
             type="text" 
             placeholder="🔍 Search by name or email..." 
             style={styles.searchInput}
             value={searchTerm}
             onChange={(e) => setSearchTerm(e.target.value)}
          />

          <div style={styles.friendsBox}>
            {isLoading ? (
                <p style={{color: "#666", textAlign: "center", marginTop: "20px"}}>Loading friends...</p>
            ) : filteredUsers.length === 0 ? (
                // Show different message depending on if they are searching or if list is empty
                <p style={styles.emptySearch}>
                    {searchTerm ? "No user found matching that name." : "No other users in the system."}
                </p>
            ) : (
                // Loop through FILTERED users, not all available users
                filteredUsers.map(user => (
                    <div key={user.id} style={styles.friendItem}>
                        <input 
                            type="checkbox" 
                            style={styles.checkbox}
                            id={`user-${user.id}`}
                            checked={selectedFriendIds.includes(user.id)}
                            onChange={() => handleCheckboxChange(user.id)}
                        />
                        <label htmlFor={`user-${user.id}`} style={{cursor:"pointer", width:"100%"}}>
                            <div style={{fontWeight:"bold", fontSize:"14px"}}>{user.name}</div>
                            <div style={{fontSize:"12px", color:"#888"}}>{user.email}</div>
                        </label>
                    </div>
                ))
            )}
          </div>
          <div style={{fontSize: "12px", color:"#888", marginTop:"5px", textAlign:"right"}}>
              {selectedFriendIds.length} person(s) selected
          </div>
        </div>

        <div style={styles.btnContainer}>
            <button type="button" onClick={() => navigate("/dashboard")} style={styles.cancelBtn}>Cancel</button>
            <button type="submit" style={styles.submitBtn}>Save Group</button>
        </div>
      </form>
    </div>
  );
};

export default CreateGroupPage;