import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const GroupDetails = () => {
  const { groupId } = useParams(); 
  const navigate = useNavigate();
  
  // --- STATE ---
  const [group, setGroup] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // --- FETCH DATA ---
  useEffect(() => {
    fetchGroupDetails();
  }, [groupId]);

  const fetchGroupDetails = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/groups/${groupId}`);
      const data = response.data.data ? response.data.data : response.data;
      setGroup(data);
    } catch (err) {
      console.error("Error fetching group:", err);
      setError("Failed to load group details.");
    } finally {
      setLoading(false);
    }
  };

  // --- NEW: REMOVE MEMBER FUNCTION ---
  const handleRemoveMember = async (memberId) => {
    // 1. Confirm intention
    if (!window.confirm("Are you sure you want to remove this member?")) {
      return;
    }

    try {
      // 2. Call Backend API
      // NOTE: Ensure your Spring Boot Controller has a DELETE mapping for this URL
      await axios.delete(`http://localhost:8080/api/groups/${groupId}/members/${memberId}`);

      // 3. Update Local State (UI) immediately
      setGroup((prevGroup) => ({
        ...prevGroup,
        members: prevGroup.members.filter((member) => member.id !== memberId),
      }));

      alert("Member removed successfully.");

    } catch (err) {
      console.error("Failed to remove member:", err);
      alert("Could not remove member. Check backend logs.");
    }
  };

  // --- STYLES ---
  const styles = {
    container: { display: "flex", height: "100vh", backgroundColor: "#fff", fontFamily: "'Segoe UI', sans-serif" },
    sidebarPlaceholder: { width: "250px", backgroundColor: "#f6f6f6", borderRight: "1px solid #ddd", padding: "20px" },
    main: { flex: 1, padding: "40px" },
    header: { borderBottom: "1px solid #eee", paddingBottom: "20px", marginBottom: "20px" },
    title: { fontSize: "28px", fontWeight: "bold", color: "#333", margin: 0 },
    subTitle: { fontSize: "14px", color: "#888", marginTop: "5px" },
    sectionTitle: { fontSize: "18px", fontWeight: "bold", marginTop: "30px", marginBottom: "15px", color: "#555" },
    memberList: { listStyle: "none", padding: 0 },
    
    // Updated Item Style for spacing
    memberItem: { 
        padding: "15px", 
        borderBottom: "1px solid #f0f0f0", 
        display: "flex", 
        alignItems: "center", 
        justifyContent: "space-between" // Pushes button to the right
    },
    
    userInfo: { display: "flex", alignItems: "center" },
    avatar: { width: "35px", height: "35px", borderRadius: "50%", backgroundColor: "#5bc5a7", color: "#fff", display: "flex", justifyContent: "center", alignItems: "center", fontWeight: "bold", marginRight: "15px" },
    backBtn: { cursor: "pointer", color: "#5bc5a7", marginBottom: "20px", display: "inline-block", fontSize: "14px" },
    
    // New Button Style
    deleteBtn: {
        backgroundColor: "transparent",
        color: "#ff4444",
        border: "1px solid #ff4444",
        borderRadius: "4px",
        padding: "5px 10px",
        cursor: "pointer",
        fontSize: "12px",
        fontWeight: "bold",
        transition: "all 0.2s"
    }
  };

  if (loading) return <div style={{padding: "40px"}}>Loading group details...</div>;
  if (error) return <div style={{padding: "40px", color: "red"}}>{error}</div>;
  if (!group) return <div style={{padding: "40px"}}>Group not found.</div>;

  return (
    <div style={styles.container}>
      <div style={styles.sidebarPlaceholder}>
        <div style={styles.backBtn} onClick={() => navigate("/dashboard")}>
          ← Back to Dashboard
        </div>
        <p style={{color:"#999", fontSize:"12px"}}>Sidebar would go here...</p>
      </div>

      <div style={styles.main}>
        {/* GROUP HEADER */}
        <div style={styles.header}>
          <h1 style={styles.title}>{group.name}</h1>
          <p style={styles.subTitle}>{group.description || "No description provided."}</p>
        </div>

        {/* MEMBERS SECTION */}
        <div>
          <h3 style={styles.sectionTitle}>Group Members ({group.members ? group.members.length : 0})</h3>
          
          <ul style={styles.memberList}>
            {group.members && group.members.length > 0 ? (
              group.members.map((member) => (
                <li key={member.id} style={styles.memberItem}>
                  {/* Left Side: Avatar & Name */}
                  <div style={styles.userInfo}>
                      <div style={styles.avatar}>
                        {member.name.charAt(0).toUpperCase()}
                      </div>
                      <div>
                        <div style={{fontWeight: "bold"}}>{member.name}</div>
                        <div style={{fontSize: "12px", color: "#999"}}>{member.email}</div>
                      </div>
                  </div>

                  {/* Right Side: Delete Button */}
                  <button 
                    style={styles.deleteBtn}
                    onClick={() => handleRemoveMember(member.id)}
                    title="Remove user from group"
                  >
                    Remove
                  </button>
                </li>
              ))
            ) : (
              <p style={{fontStyle:"italic", color:"#999"}}>No members found.</p>
            )}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default GroupDetails;