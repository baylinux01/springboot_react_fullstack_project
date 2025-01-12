import React, { useEffect,useState } from 'react';
import {useParams,useNavigate,Link} from "react-router-dom";
import axios from "axios";
import logo from './logo.svg';
import './App.css';
import Navbar from './Navbar';
import { BrowserRouter,Routes,Switch,Route } from 'react-router-dom';
import Home from './Home';
import Groups from './Groups';
import Group from "./Group";
import User from "./User";
import Register from "./Register";
import Login from "./Login";
import Users from "./Users"
import Message from './Message';
import _ from "lodash";

//react'ta lodash kütüphanesi objeleri karşılaştırmaya veya
//bir dizi içerisinde belli bir objenin olup olmadığını anlamaya yarar
//lodash kütüphanesi npm install lodash kodu ile indirilebilir
//sonra sayfa başında import _ from lodash yaparak kullanılabilir
//_.isEqual() ve _.find metotları lodah kütüphanesine aittir.

//axios kütüphanesi npm install axios kodu ile indirilebilir.
//qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
//qs kullanmayınca post isteklerinde veriler api'ya null gidiyor

//react'ta bir objenin null olup olmadığını kontrol etmek için Object.keys(obje).length===0 kullanılıyor.
//react'ta objeyi setter ile güncellerken özel bir kullanım var
//obje setObject((object)=>({...updatedValue})) şeklinde güncellenir.

//react'ta bir arrayi güncellerken setArray([...response.data]) şeklinde güncellemek gerekiyor
//aksi takdirde api'dan obje geldiği için array objeye dönüşebiliyor. bu da hata fırlatıyor.

function App() {

  const[unsuccessfulsignin,setUnsuccessfulsignin]=useState(false);
  const [signupformsubmitresult,setSignupformsubmitresult]=useState("");
  const [user,setUser]=useState({});
  const [users,setUsers]=useState([]);
  const [messages,setMessages]=useState([]);
  const [connectionRequests,setConnectionRequests]=useState([]);
  const [connectionsOfUser,setConnectionsOfUser]=useState([]);
  const[groups,setGroups]=useState([]);
  const [group,setGroup]=useState({});
  const [user2,setUser2]=useState({});
  const[blockedUsersOfUser,setBlockedUsersOfUser]=useState({});
  const[password,setPassword]=useState("");
  

  function fetchGroups(){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/groups/getallgroups").then((response)=>
      {setGroups([...response.data])});
  }

 
    


  return (
    <div>
    <BrowserRouter>
    <Navbar user={user} setUser={setUser} user2={user2} setUser2={setUser2} password={password} setPassword={setPassword} ></Navbar>
    
    <Routes>

    <Route exact path="/" element={<Home 
    group={group} setGroup={setGroup} groups={groups} setGroups={setGroups} unsuccessfulsignin={unsuccessfulsignin} setUnsuccessfulsignin={setUnsuccessfulsignin} 
    user={user} setUser={setUser} password={password} setPassword={setPassword}
    signupformsubmitresult={signupformsubmitresult} setSignupformsubmitresult={setSignupformsubmitresult}>
    </Home>}></Route>

    <Route exact path="/groups" element={<Groups 
    groups={groups} setGroups={setGroups} 
    unsuccessfulsignin={unsuccessfulsignin} setUnsuccessfulsignin={setUnsuccessfulsignin} 
    user={user} setUser={setUser} password={password} setPassword={setPassword}
    signupformsubmitresult={signupformsubmitresult} setSignupformsubmitresult={setSignupformsubmitresult}>
    </Groups>}></Route>

    <Route exact path="/groups/group/:groupId" element={<Group 
    group={group} setGroup={setGroup}
    user={user} setUser={setUser} password={password} setPassword={setPassword}
    blockedUsersOfUser={blockedUsersOfUser} setBlockedUsersOfUser={setBlockedUsersOfUser}>
    </Group>}></Route>

    
    <Route
    exact path='/users/users' element={<Users
      setConnectionsOfUser={setConnectionsOfUser}
      connectionsOfUser={connectionsOfUser}
      connectionRequests={connectionRequests}
      setConnectionRequests={setConnectionRequests}
      users={users} setUsers={setUsers}
      user2={user2} setUser2={setUser2} 
      blockedUsersOfUser={blockedUsersOfUser} setBlockedUsersOfUser={setBlockedUsersOfUser}
      unsuccessfulsignin={unsuccessfulsignin} setUnsuccessfulsignin={setUnsuccessfulsignin} 
      user={user} setUser={setUser} password={password} setPassword={setPassword}
      signupformsubmitresult={signupformsubmitresult} setSignupformsubmitresult={setSignupformsubmitresult}>
    </Users>}>
    </Route>
    <Route exact path="/users/user/:user2Id" element={<User 
    setConnectionsOfUser={setConnectionsOfUser}
    connectionsOfUser={connectionsOfUser}
    connectionRequests={connectionRequests}
    setConnectionRequests={setConnectionRequests}
    user2={user2} setUser2={setUser2} 
    unsuccessfulsignin={unsuccessfulsignin} setUnsuccessfulsignin={setUnsuccessfulsignin} 
    user={user} setUser={setUser} password={password} setPassword={setPassword}
    blockedUsersOfUser={blockedUsersOfUser} setBlockedUsersOfUser={setBlockedUsersOfUser}
    signupformsubmitresult={signupformsubmitresult} setSignupformsubmitresult={setSignupformsubmitresult}>
    </User>}></Route>

    <Route exact path="/message/:user2Id" element={<Message
    messages={messages}
    setMessages={setMessages}
    setConnectionsOfUser={setConnectionsOfUser}
    connectionsOfUser={connectionsOfUser}
    connectionRequests={connectionRequests}
    setConnectionRequests={setConnectionRequests}
    user2={user2} setUser2={setUser2} 
    unsuccessfulsignin={unsuccessfulsignin} setUnsuccessfulsignin={setUnsuccessfulsignin} 
    user={user} setUser={setUser} password={password} setPassword={setPassword}
    signupformsubmitresult={signupformsubmitresult} setSignupformsubmitresult={setSignupformsubmitresult}>
    </Message>}></Route>

    <Route exact path="/register" element={<Register 
    unsuccessfulsignin={unsuccessfulsignin} setUnsuccessfulsignin={setUnsuccessfulsignin} 
    user={user} setUser={setUser} password={password} setPassword={setPassword}
    signupformsubmitresult={signupformsubmitresult} setSignupformsubmitresult={setSignupformsubmitresult} >
    </Register>}></Route>

    <Route exact path="/login" element={<Login 
    unsuccessfulsignin={unsuccessfulsignin} setUnsuccessfulsignin={setUnsuccessfulsignin} 
    user={user} setUser={setUser} password={password} setPassword={setPassword} signupformsubmitresult={signupformsubmitresult} 
    setSignupformsubmitresult={setSignupformsubmitresult}>
    </Login>}></Route>
    
    </Routes>
    </BrowserRouter>
    </div>
    
    
  );
}

export default App;
