import React, { useCallback, useEffect,useState } from 'react';
import {useParams,useNavigate,Link} from "react-router-dom";
import axios from "axios";
import _ from "lodash";
import { Modal,Table,Button, ListGroup } from 'react-bootstrap';


export default function Home(
  {password,setPassword,group,setgroup,groups,setGroups,signupformsubmitresult,setSignupformsubmitresult,
    user,setUser,unsuccessfulsignin,setUnsuccessfulsignin}
  ) {
  
  const [showPopUp,setShowPopUp]=useState(false);
  const[file,setFile]=useState(null);
  const [showPopUp2,setShowPopUp2]=useState(false);
  const [newGroupName,setNewGroupName]=useState("");
  function fetchGroups(){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/groups/getallgroups").then((response)=>
      {setGroups([...response.data]);});
    
  }

  function fetchUser(){
    axios.defaults.baseURL="http://localhost:8080";
    
    axios.get("/users/getoneuserbyid",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userId:localStorage.getItem("id")}})
    .then((response)=>{setUser({...response.data})});
  }
  
  
  useEffect(()=>{
    
    if(localStorage.getItem("id")!=null)
    {
      fetchUser();
      
      
    }
    fetchGroups();
   
    
  },[]);

   
  function deleteGroup(groupId)
      {
          
          
        //axios kütüphanesi npm install axios kodu ile indirilebilir.
      
       axios.defaults.baseURL="http://localhost:8080";
       axios.delete("/groups/deletegroupbyid",
        {auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{groupId:groupId}});
      
      fetchGroups();
        
      }

      function joinGroup(groupId,userId)
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
          //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
          //qs kullanmayınca post isteklerinde veriler api'ya null gidiyor
        

         axios.defaults.baseURL="http://localhost:8080";
      const qs=require('qs');
      axios.post("/groups/beamemberofgroup", 
        qs.stringify( {groupId: groupId
        })
      ,{
        auth: {
          username: localStorage.getItem("username"),
          password: localStorage.getItem("password")
        }
      });
         fetchGroups();
        }

        function leaveGroup(groupId,userId)
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
          //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
          //qs kullanmayınca post isteklerinde veriler api'ya null gidiyor
         

         axios.defaults.baseURL="http://localhost:8080";
    const qs=require('qs');
    axios.post("/groups/exitgroup", 
      qs.stringify({groupId: groupId
      })
    ,{
      auth: {
        username: localStorage.getItem("username"),
        password: localStorage.getItem("password")
      }
    });
         fetchGroups();
        }

        function changePhoto()
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
          //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
          //qs kullanmayınca post isteklerinde veriler api'ya null gidiyor
         const formData=new FormData();
         formData.append("newuserimage",file);

         axios.defaults.baseURL="http://localhost:8080";
    const qs=require('qs');
    axios.put("/users/changeuserimage", 
      formData,{
        headers:{
          "Content-Type":"multipart/form-data"
        }
      
    ,
      auth: {
        username: localStorage.getItem("username"),
        password: localStorage.getItem("password")
      }
    }).then(response=>console.log(response.data));
          fetchUser();
         fetchGroups();
         setShowPopUp(false);
         //window.history.go(0);
         
  }
 
  function createthegroup()
    {
        
        
      //axios kütüphanesi npm install axios kodu ile indirilebilir.
      //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.qs kullanmayınca veriler api'ya null gidiyor
      axios.defaults.baseURL="http://localhost:8080";
      const qs=require('qs');
      axios.post("/groups/creategroup", 
        qs.stringify( {name: newGroupName})
      ,{
        auth: {
          username: localStorage.getItem("username"),
          password: localStorage.getItem("password")
        }
      });
      
      fetchGroups();
      fetchUser();
      setShowPopUp2(false);
      //window.history.go(0);
      
      
      
      //react'ta bir objenin null olup olmadığını kontrol etmek için Object.keys(obje).length===0 kullanılıyor.
      //react'ta objeyi setter ile güncellerken özel bir kullanım var
      //obje setObject(object=>({...object,...updatedValue})) şeklinde güncellenir.
       
    }
  
  return (
    <>
    <div>
      {Object.keys(user).length!==0?
      <div>
      <img style={{position:"absolute",height:"385px",width:"400px",
        top:"160px",left:"100px"
      }} src={`data:image/*;base64,${user.userImage}`}/>
       <Button style={{position:"absolute",top:"100px",left:"100px"}} variant="primary" onClick={()=>setShowPopUp(true)}>Change Photo</Button>
      </div>
      :
      <div></div>
    }
   
    <Modal show={showPopUp} onHide={()=>setShowPopUp(false)}>
    <Modal.Header>
      <Modal.Title>Change Photo</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <input type='file' id='file' onChange={(e)=>setFile(e.target.files[0])}></input>
      
    </Modal.Body>
    <Modal.Footer>
    <Button variant='primary' onClick={()=>{changePhoto()}}>Change Photo</Button>
    <Button variant='primary' onClick={()=>{setShowPopUp(false)}}>Close Pop-Up</Button>
    </Modal.Footer>
    </Modal>
    </div>
    
    <div style={{ marginTop:"100px",marginLeft:"550px",
    width:"400px",minHeight:"500px",height:"auto",textDecoration:"none"}}>
      {
      Object.keys(user).length!==0 ?
      <div>
        {groups.filter(group=>
      
      _.isEqual(group.owner,user)).length>0?
        <div>Kurucusu olduğunuz gruplar:</div>:<div></div>}
      <div> {groups.filter(group=>
      
         _.isEqual(group.owner,user)
        
  ).map(g=>
 
    <div key={g.id}>
           
           <Table striped bordered hover>
      <thead>
     
      </thead>
      <tbody>
        <tr>
        
          <td style={{textAlign:"left",LineHeight:"50px",
            textDecoration:"none",width:"60%",borderRight:"none"
          }}><Link to={"/groups/group/"+g.id} >{g.name}</Link></td>
           
           <td style={{display:"flex",justifyContent:"right",
            alignItems:"center",textDecoration:"none",width:"auto",borderLeft:"none"
          }}><Button variant="danger" 
          sync="true" 
          onClick={()=>deleteGroup(g.id)}>Delete Group</Button></td>
          </tr>
       
       </tbody>
     </Table>
          
          </div>
  )}
      </div>
      {groups.filter(group=>
      
      !_.isEqual(group.owner,user) && _.find(group.members,user)).length>0?
      <div>Üye olduğunuz gruplar:</div>:<div></div>}
      <div> {groups.filter(group=>
      
      !_.isEqual(group.owner,user) && _.find(group.members,user)
     
).map(g=>

 <div key={g.id}>
    
           <Table striped bordered hover>
      <thead>
     
      </thead>
      <tbody>
        <tr>
        
          <td style={{textAlign:"left",LineHeight:"50px",
            textDecoration:"none",width:"60%",borderRight:"none"
          }}><Link to={"/groups/group/"+g.id}>{g.name}</Link></td>
          
           
           <td style={{display:"flex",justifyContent:"right",
            alignItems:"center",textDecoration:"none",width:"auto",borderLeft:"none"
          }}><Button variant="warning" 
          sync="true" 
          onClick={()=>leaveGroup(g.id,user.id)}>Leave Group</Button></td>
          
        </tr>
       
      </tbody>
    </Table>
       </div>
)}
   </div>
   <Modal show={showPopUp2} onHide={()=>setShowPopUp2(false)}>
    <Modal.Header>
      <Modal.Title>Create New Group</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <input type='text' id='newmessageContent' onChange={(e)=>setNewGroupName(e.target.value)}></input>
      
    </Modal.Body>
    <Modal.Footer>
    <Button variant='primary' onClick={()=>{createthegroup()}}>Create Group</Button>
    <Button variant='primary' onClick={()=>{setShowPopUp2(false)}}>Close Pop-Up</Button>
    </Modal.Footer>
    </Modal>
      <Button variant="primary" onClick={()=>setShowPopUp2(true)}>Create New Group</Button>
      </div>
       : <div>Home</div>
    }
    </div>
    </>
    
   
  )
}
