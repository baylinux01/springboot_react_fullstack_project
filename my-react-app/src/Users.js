import React, { useEffect,useState } from 'react';
import {useParams,useNavigate,Link} from "react-router-dom";
import axios from "axios";
import _ from "lodash";
import { Modal,Table,Button, ListGroup, ListGroupItem } from 'react-bootstrap';

export default function Users({ connectionsOfUser,setConnectionsOfUser, connectionRequests,setConnectionRequests,password,setPassword,group,setgroup,groups,setGroups,signupformsubmitresult,setSignupformsubmitresult,
    users,setUsers,user,setUser,unsuccessfulsignin,setUnsuccessfulsignin,blockedUsersOfUser,setBlockedUsersOfUser}) {
        
      const [showPopUp,setShowPopUp]=useState(false);
      const [showPopUp2,setShowPopUp2]=useState(false);
      const [showPopUp3,setShowPopUp3]=useState(false);
      const [user3Id,setUser3Id]=useState();
      const [blockedUsersOfUser2,setBlockedUsersOfUser2]=useState([]);

      function fetchBlockedUsersOfUser(){
        axios.defaults.baseURL="http://localhost:8080";
        axios.get("/users/getblockedusersofcurrentuser",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{}})
        .then((response)=>{setBlockedUsersOfUser([...response.data])});
        
      }
      function fetchBlockedUsersOfUser2(id){
        axios.defaults.baseURL="http://localhost:8080";
        axios.get("/users/getblockedusersofauser",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userId:id}})
        .then((response)=>{setBlockedUsersOfUser2([...response.data])});
      }
    function blockUser(id)
    {

     const params=new URLSearchParams();
     params.append("userToBeBlockedId",id);
     
     
     axios.defaults.baseURL="http://localhost:8080";
     axios.post("/users/blockuser", 
      params
    ,{
      auth: {
        username: localStorage.getItem("username"),
        password: localStorage.getItem("password")
      }
      
    });
      
    
      fetchUser();
      fetchBlockedUsersOfUser();
      getAllConnectionRequests();
      getAllConnectionsOfCurrentUser();
      
    }
    function unblockUser(id)
    {

     const params=new URLSearchParams();
     params.append("userToBeUnblockedId",id);
     
     
     axios.defaults.baseURL="http://localhost:8080";
     axios.post("/users/unblockuser", 
      params
    ,{
      auth: {
        username: localStorage.getItem("username"),
        password: localStorage.getItem("password")
      }
      
    });
      
    
      fetchUser();
      fetchBlockedUsersOfUser();
      getAllConnectionRequests();
      getAllConnectionsOfCurrentUser();
      
    }
      
        function fetchUser(){
            axios.defaults.baseURL="http://localhost:8080";
            
            axios.get("/users/getoneuserbyid",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userId:localStorage.getItem("id")}})
            .then((response)=>{setUser({...response.data})});
          }
      function fetchUsers(){
        axios.defaults.baseURL="http://localhost:8080";
        
        axios.get("/users/getallusers")//,{auth: {username: user.username,password: password}})
        .then((response)=>{setUsers([...response.data])});
      }
      function getAllConnectionRequests(){
        axios.defaults.baseURL="http://localhost:8080";
        
        axios.get("/connectionrequests/getallconnectionrequests",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")}})
        .then((response)=>{setConnectionRequests([...response.data])});
      }
      function getAllConnectionsOfCurrentUser(){
        axios.defaults.baseURL="http://localhost:8080";
        
        axios.get("/users/getconnectionsofauser",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userId:localStorage.getItem("id")}})
        .then((response)=>{setConnectionsOfUser([...response.data])});
      }
     useEffect(() => {
        if(localStorage.getItem("id")!=null)
            {
              fetchUser();
              getAllConnectionRequests();
              getAllConnectionsOfCurrentUser();
              fetchBlockedUsersOfUser();
            }
       fetchUsers();
       
     
       
     }, [])

     
     
     function sendConnectionRequest(user2Id)
        {
         
        const params=new URLSearchParams();
        params.append("connectionRequestReceiverId",user2Id);

         axios.defaults.baseURL="http://localhost:8080";
      //const qs=require('qs');
      axios.post("/connectionrequests/createconnectionrequest", 
        params
      ,{
        auth: {
          username: localStorage.getItem("username"),
          password: localStorage.getItem("password")
        }
      });

          getAllConnectionRequests();
          getAllConnectionsOfCurrentUser();
         
        }
        function acceptConnectionRequest(user2Id)
        {
         
        const params=new URLSearchParams();
        params.append("userToBeAcceptedId",user2Id);

         axios.defaults.baseURL="http://localhost:8080";
      //const qs=require('qs');
      axios.post("/users/acceptconnection", 
        params
      ,{
        auth: {
          username: localStorage.getItem("username"),
          password: localStorage.getItem("password")
        }
      });

          getAllConnectionRequests();
          getAllConnectionsOfCurrentUser();
         
        }
      
        function deleteConnection(Id)
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
        
         axios.defaults.baseURL="http://localhost:8080";
         axios.delete("/users/deleteconnection",
          {auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userToBeDeletedId:Id}});
        
        
          getAllConnectionRequests();
          getAllConnectionsOfCurrentUser();
         
        }
        function refuseConnectionRequest(Id)
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
        
         axios.defaults.baseURL="http://localhost:8080";
         axios.delete("/connectionrequests/refuseconnectionrequest",
          {auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{connectionRequestSenderId:Id}});
        
        
          getAllConnectionRequests();
          getAllConnectionsOfCurrentUser();
         
        }
        function cancelConnectionRequest(Id)
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
        
         axios.defaults.baseURL="http://localhost:8080";
         axios.delete("/connectionrequests/cancelconnectionrequest",
          {auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{connectionRequestReceiverId:Id}});
        
        
          getAllConnectionRequests();
          getAllConnectionsOfCurrentUser();
         
        }
        let conreq1=null;
        let conreq2=null;
  return (
   
    
    <ListGroup style={{marginTop:"100px",marginLeft:"450px",width:"400px",height:"auto"}}>
        <ListGroupItem>
        <Modal show={showPopUp} onHide={()=>setShowPopUp(false)}>
          <Modal.Header>
            <Modal.Title>Users</Modal.Title>
          </Modal.Header>
          <Modal.Body>
          
          {users.map(u=>{
            
          return _.isEqual(user,u)?
          <div></div>
          :!_.find(blockedUsersOfUser,u)?
            <div>
              <ListGroupItem key={u.id} style={{display:"flex",justifyContent:"space-between"}}>
             <img style={{height:"40px",width:"40px"
         
       }} src={`data:image/*;base64,${u.userImage}`}/>
           <Link to={"/users/user/"+u.id} >{u.name+" "+u.surname}</Link>
           <Button variant="danger" sync="true" onClick={()=>blockUser(u.id)}>Block</Button>
            </ListGroupItem>
            </div>
            :_.find(blockedUsersOfUser,u)?
            <div>
                <ListGroupItem key={u.id} style={{display:"flex",justifyContent:"space-between"}}>
             <img style={{height:"40px",width:"40px"
         
       }} src={`data:image/*;base64,${u.userImage}`}/>
           <Link to={"/users/user/"+u.id} >{u.name+" "+u.surname}</Link>
           <Button variant="danger" sync="true" onClick={()=>unblockUser(u.id)}>UnBlock</Button>
            </ListGroupItem>
            </div>
            :
            <div>
                <ListGroupItem key={u.id} style={{display:"flex",justifyContent:"space-between"}}>
             <img style={{height:"40px",width:"40px"
         
       }} src={`data:image/*;base64,${u.userImage}`}/>
           <Link to={"/users/user/"+u.id} >{u.name+" "+u.surname}</Link>
           <Button variant="danger" sync="true" onClick={()=>unblockUser(u.id)}>UnBlock</Button>
            </ListGroupItem>
            </div>
          
          
        })}
          
            
          </Modal.Body>
          <Modal.Footer>
          
          <Button variant='primary' onClick={()=>{setShowPopUp(false)}}>Close Pop-Up</Button>
          </Modal.Footer>
          </Modal>
          </ListGroupItem>
         
         {users.map(u=>{
          
         
        //fetchBlockedUsersOfUser2(u.id);
          // getAllConnectionRequests();
          // getAllConnectionsOfCurrentUser();
          //fetchConreqWhoseSenderIsCurrentUser(u.id);
          //fetchConreqWhoseReceiverIsCurrentUser(u.id);
            conreq1=connectionRequests.filter(req=>req.connectionRequestSender.id==user.id&&req.connectionRequestReceiver.id==u.id);
            conreq2=connectionRequests.filter(req=>req.connectionRequestSender.id==u.id&&req.connectionRequestReceiver.id==user.id);
            if(conreq1!=null&conreq1[0]!=null) conreq1=conreq1[0];
            if(conreq2!=null&conreq2[0]!=null) conreq2=conreq2[0];
           
         return _.isEqual(u,user)?
         <div></div>
         :!_.isEqual(u,user) && _.find(connectionsOfUser,u)?
          
           <ListGroupItem key={u.id} style={{display:"flex",justifyContent:"space-between"}}>
             <img style={{height:"40px",width:"40px"
         
       }} src={`data:image/*;base64,${u.userImage}`}/>
           <Link to={"/users/user/"+u.id} >{u.name+" "+u.surname}</Link>
           <Link to={"/message/"+u.id}><Button variant="primary" sync="true" >Message</Button></Link>
           <Button variant="danger" sync="true" onClick={()=>deleteConnection(u.id)}>Disconnect</Button>
           <Button variant="danger" sync="true" onClick={()=>{setShowPopUp(true)}}>Block</Button>
            </ListGroupItem>
         :!_.isEqual(u,user) && !_.find(connectionsOfUser,u)&&_.isEqual(conreq1.connectionRequestSender,user)?
          
            <ListGroupItem key={u.id} style={{display:"flex",justifyContent:"space-between"}}>
              <img style={{height:"40px",width:"40px"
          
        }} src={`data:image/*;base64,${u.userImage}`}/>
            <Link to={"/users/user/"+u.id} >{u.name+" "+u.surname}</Link>
            <Button variant="danger" sync="true" onClick={()=>cancelConnectionRequest(u.id)}>Cancel Connection Request</Button>
            <Button variant="danger" sync="true" onClick={()=>{setShowPopUp(true)}}>Block</Button>
             </ListGroupItem>
             :!_.isEqual(u,user) && !_.find(connectionsOfUser,u)&&_.isEqual(conreq2.connectionRequestReceiver,user)?
          
             <ListGroupItem key={u.id} style={{display:"flex",justifyContent:"space-between"}}>
               <img style={{height:"40px",width:"40px"
           
         }} src={`data:image/*;base64,${u.userImage}`}/>
             <Link to={"/users/user/"+u.id} >{u.name+" "+u.surname}</Link>
             <Button variant="success" sync="true" onClick={()=>acceptConnectionRequest(u.id)}>Accept Connection Request</Button>
             <Button variant="danger" sync="true" onClick={()=>refuseConnectionRequest(u.id)}>Refuse Connection Request</Button>
             <Button variant="danger" sync="true" onClick={()=>{setShowPopUp(true)}}>Block</Button>
              </ListGroupItem>
          :!_.isEqual(u,user) && !_.find(connectionsOfUser,u)
          &&!_.find(blockedUsersOfUser2,user)&&!_.find(blockedUsersOfUser,u)?
          
          <ListGroupItem key={u.id} style={{display:"flex",justifyContent:"space-between"}}>
            <img style={{height:"40px",width:"40px"
        
      }} src={`data:image/*;base64,${u.userImage}`}/>
          <Link to={"/users/user/"+u.id} >{u.name+" "+u.surname}</Link>
          <Button variant="warning" sync="true" onClick={()=>sendConnectionRequest(u.id)}>Connect</Button>
          <Button variant="danger" sync="true" onClick={()=>{setShowPopUp(true)}}>Block</Button>
           </ListGroupItem>
           :!_.isEqual(u,user) && !_.find(connectionsOfUser,u)
           &&!_.find(blockedUsersOfUser2,user)&&_.find(blockedUsersOfUser,u)?
          
           <ListGroupItem key={u.id} style={{display:"flex",justifyContent:"space-between"}}>
             <img style={{height:"40px",width:"40px"
         
       }} src={`data:image/*;base64,${u.userImage}`}/>
           <Link to={"/users/user/"+u.id} >{u.name+" "+u.surname}</Link>
           
           <Button variant="danger" sync="true" onClick={()=>{setShowPopUp(true)}}>Unblock</Button>
            </ListGroupItem>
           :!_.isEqual(u,user) && !_.find(connectionsOfUser,u)&&_.find(blockedUsersOfUser2,user)?
           <ListGroupItem key={u.id} style={{display:"flex",justifyContent:"space-between"}}>
            <img style={{height:"40px",width:"40px"
      }} src={`data:image/*;base64,${u.userImage}`}/>
          <Link></Link>
          <Button variant="success" sync="true" ></Button>
           </ListGroupItem>
           :
           <ListGroupItem key={u.id} style={{display:"flex",justifyContent:"space-between"}}>
            <img style={{height:"40px",width:"40px"
      }} src={`data:image/*;base64,${u.userImage}`}/>
          <Link to={"/users/user/"+u.id} >{u.name+" "+u.surname}</Link>
          <Button variant="success" sync="true" ></Button>
           </ListGroupItem>
          conreq1=null;
          conreq2=null;
    }
          
    
         
          
    )} 
     
   
    
    </ListGroup>
  )
}
