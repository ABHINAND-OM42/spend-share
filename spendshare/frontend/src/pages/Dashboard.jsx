import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const navigate = useNavigate();
  
  // --- STATE ---
  const [currentUser, setCurrentUser] = useState(null);
  const [groups, setGroups] = useState([]); 
  const [friends, setFriends] = useState([]); 
  const [isLoading, setIsLoading] = useState(true); 
  const [error, setError] = useState(null);

  // --- NEW: SEARCH STATE ---
  const [searchTerm, setSearchTerm] = useState(""); 

  // --- INIT DATA ---
  useEffect(() => {
    const userStr = localStorage.getItem("currentUser");
    if (!userStr) {
      navigate("/login");
      return;
    }
    const user = JSON.parse(userStr);
    setCurrentUser(user);
    fetchInitialData(user);
  }, [navigate]);

  const fetchInitialData = async (user) => {
    setIsLoading(true);
    try {
      await Promise.all([fetchGroups(), fetchFriends(user)]);
    } catch (err) {
      console.error("Error loading dashboard data:", err);
      setError("Could not load data.");
    } finally {
      setIsLoading(false);
    }
  };

  const fetchGroups = async () => {
    const response = await axios.get("http://localhost:8080/api/groups");
    const groupData = response.data.data ? response.data.data : response.data;
    setGroups(Array.isArray(groupData) ? groupData : []);
  };

  const fetchFriends = async (user) => {
    const response = await axios.get("http://localhost:8080/api/users");
    const usersList = response.data.data ? response.data.data : response.data;
    if (Array.isArray(usersList)) {
      const otherUsers = usersList.filter(u => u.id !== user.id);
      setFriends(otherUsers);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("currentUser");
    navigate("/login");
  };

  // --- NEW: FILTER LOGIC ---
  // We filter the groups based on the name typing
  const filteredGroups = groups.filter(group => 
    group.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // --- STYLES ---
  const styles = {
    container: { display: "flex", height: "100vh", fontFamily: "'Segoe UI', sans-serif", backgroundColor: "#fff" },
    sidebar: { width: "250px", backgroundColor: "#f6f6f6", padding: "20px", borderRight: "1px solid #ddd", display: "flex", flexDirection: "column" },
    sidebarHeader: { fontSize: "12px", color: "#999", fontWeight: "bold", marginTop: "20px", marginBottom: "10px", display: "flex", justifyContent: "space-between", alignItems: "center" },
    navItem: { padding: "8px 0", cursor: "pointer", color: "#333", fontSize: "14px", display: "flex", alignItems: "center" },
    navIcon: { marginRight: "10px", fontSize: "16px" },
    addButton: { cursor: "pointer", color: "#333", fontSize: "10px", backgroundColor:"#e1e1e1", padding:"2px 6px", borderRadius:"4px", border:"1px solid #ccc"},
    main: { flex: 1, padding: "0 40px", overflowY: "auto" },
    topBar: { display: "flex", justifyContent: "space-between", alignItems: "center", padding: "20px 0", borderBottom: "1px solid #eee" },
    logoutBtn: { cursor: 'pointer', border: 'none', background: 'none', color: '#ff4444', fontWeight: 'bold' },
    loading: { fontSize: "12px", color: "#999", padding: "10px 0" },
    empty: { fontSize: "12px", color: "#999", fontStyle: "italic", padding: "5px 0" },
    
    // --- NEW SEARCH STYLE ---
    searchInput: {
      width: "100%",
      padding: "8px",
      marginTop: "5px",
      marginBottom: "10px",
      borderRadius: "4px",
      border: "1px solid #ddd",
      fontSize: "13px"
    }
  };

  return (
    <div style={styles.container}>
      
      {/* --- LEFT SIDEBAR --- */}
      <div style={styles.sidebar}>
        <h2 style={{ color: "#5bc5a7", marginBottom: "20px" }}>SpendShare</h2>
        
        <div style={styles.navItem}><span style={styles.navIcon}>📊</span> Dashboard</div>
        <div style={styles.navItem}><span style={styles.navIcon}>🚩</span> Recent Activity</div>

        {/* GROUPS HEADER */}
        <div style={styles.sidebarHeader}>
          <span>GROUPS</span> 
          <span style={styles.addButton} onClick={() => navigate("/create-group")}>+ add</span>
        </div>

        {/* --- NEW: SEARCH BAR --- */}
        <input 
            type="text" 
            placeholder="Search groups..." 
            style={styles.searchInput}
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
        />
        
        {/* GROUPS LIST (Use filteredGroups instead of groups) */}
        {isLoading ? (
          <div style={styles.loading}>Loading...</div>
        ) : filteredGroups.length === 0 ? (
          <div style={styles.empty}>
            {searchTerm ? "No match found" : "No groups yet"}
          </div>
        ) : (
          filteredGroups.map(g => (
            <div 
              key={g.id} 
              style={styles.navItem} 
              onClick={() => navigate(`/group/${g.id}`)}
            >
              <span style={styles.navIcon}>🏷️</span> {g.name}
            </div>
          ))
        )}

        <div style={styles.sidebarHeader}>FRIENDS</div>
        {isLoading ? <div style={styles.loading}>Loading...</div> : friends.map(friend => (
            <div key={friend.id} style={styles.navItem}>
              <span style={styles.navIcon}>👤</span> {friend.name}
            </div>
        ))}
      </div>

      {/* --- MAIN CONTENT --- */}
      <div style={styles.main}>
        <div style={styles.topBar}>
          <div style={{ fontSize: "24px", fontWeight: "bold" }}>Dashboard</div>
          <button onClick={handleLogout} style={styles.logoutBtn}>Logout</button>
        </div>
        
        {error && <div style={{color: "red", marginTop:"20px"}}>{error}</div>}

        <div style={{ marginTop: "50px", textAlign: "center", color: "#888" }}>
          <h3 style={{color: "#333"}}>Welcome, {currentUser?.name}!</h3>
          <p>You are all set.</p>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;