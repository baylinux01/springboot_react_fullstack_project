
import React, { useEffect,useState } from 'react';
import {useParams,Link} from "react-router-dom";
import axios from "axios";
import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import 'bootstrap/dist/css/bootstrap.min.css';
import _ from "lodash";
import { Modal, ListGroup, ListGroupItem } from 'react-bootstrap';
import moment from "moment";



export default function Group({password,setPassword,group,setGroup,user,setUser,blockedUsersOfUser,setBlockedUsersOfUser}) {

  
const[newCommentContent,setNewCommentContent] =useState("");
const[commentToBeQuoted,setCommentToBeQuoted]=useState({content:""});
const [commentContentToBeEdited,setCommentContentToBeEdited]=useState("");
const[mediaToBeQuoted,setMediaToBeQuoted]=useState({content_type:""});
const[groupMembers,setGroupMembers]=useState([]);
const[groupComments,setGroupComments]=useState([]);
const [groupPosts,setGroupPosts]=useState([]);
const[blockedUsersOfCommentOwner,setBlockedUsersOfCommentOwner]=useState([]);
const [showPopUp,setShowPopUp]=useState(false);
const [showPopUp2,setShowPopUp2]=useState(false);
const [showPopUp3,setShowPopUp3]=useState(false);
const [user3Id,setUser3Id]=useState();
const [permissionsOfAUserForAGroup,setPermissionsOfAUserForAGroup]=useState("");
const [permissionsOfCurrentUserForAGroup,setPermissionsOfCurrentUserForAGroup]=useState("");

const[file,setFile]=useState(null);
const[imageUrl,setImageUrl]=useState("");

const{groupId}=useParams();

function fetchGroup(){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/groups/getonegroupbyid",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{groupId:groupId}}).then((response)=>{setGroup({...response.data})});
  }
function fetchComments(){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/comments/getcommentsofagroup",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{groupId:groupId}}).then((response)=>{setGroupComments([...response.data])});
  }
  function fetchPostsOfAGroup(){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/posts/getpostsofagroup",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{groupId:groupId}}).then((response)=>{setGroupPosts([...response.data])});
  }
  function downloadFile(fileName){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/medias/downloadfilefaster"
      ,{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")}
      ,params:{fileName:fileName},responseType:'blob',
    headers:{
      "Content-Type":"application/octet-stream"
    }}).then(response=>{
        const url = window.URL.createObjectURL(new Blob([response.data])); 
        const link = document.createElement('a'); 
        link.href = url; link.setAttribute('download', fileName); 
        document.body.appendChild(link); 
        link.click();
      });
  }
  function fetchMembers(){
    axios.defaults.baseURL="http://localhost:8080";
    return axios.get("/groups/getonegroupmembersbygroupid",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{groupId:groupId}}).then((response)=>{setGroupMembers([...response.data])});
  }

  function fetchUser(){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/users/getoneuserbyid",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userId:localStorage.getItem("id")}})
    .then((response)=>{setUser({...response.data})});
    console.log(user)
  }

  function fetchBlockedUsersOfUser(){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/users/getblockedusersofcurrentuser",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{}})
    .then((response)=>{setBlockedUsersOfUser([...response.data])});
    
  }

  function fetchBlockedUsersOfCommentOwner(id){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/users/getblockedusersofauser",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userId:id}})
    .then((response)=>{setBlockedUsersOfCommentOwner([...response.data])});
    
  }
  function fetchPermissionsOfAUserForAGroup(id){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/usergrouppermissions/getpermissionsofauserforagroup",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userId:id,groupId:groupId}})
    .then((response)=>{setPermissionsOfAUserForAGroup(response.data)});
    
  }
  function fetchPermissionsOfCurrentUserForAGroup(){
    axios.defaults.baseURL="http://localhost:8080";
    axios.get("/usergrouppermissions/getpermissionsofauserforagroup",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userId:localStorage.getItem("id"),groupId:groupId}})
    .then((response)=>{setPermissionsOfCurrentUserForAGroup(response.data)});
    console.log(permissionsOfCurrentUserForAGroup);
    
  }
  function deleteComment(commentId)
  {
    axios.defaults.baseURL="http://localhost:8080";
    axios.delete("/comments/deletecomment",
      {auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{commentId:commentId}});
    fetchComments();
    fetchPostsOfAGroup();
    // axios.defaults.baseURL="http://localhost:8080";
    // const qs=require('qs');
    // axios.delete("/comments/deletecomment", 
    //   qs.stringify({commentId:commentId
    //   })
    // ,{
    //   auth: {
    //     username: user.username,
    //     password: password
    //   }
    // });
  }
  function deleteMedia(mediaId)
  {
    axios.defaults.baseURL="http://localhost:8080";
    axios.delete("/medias/deletemedia",
      {auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{id:mediaId}});
    fetchComments();
    fetchPostsOfAGroup();
    // axios.defaults.baseURL="http://localhost:8080";
    // const qs=require('qs');
    // axios.delete("/comments/deletecomment", 
    //   qs.stringify({commentId:commentId
    //   })
    // ,{
    //   auth: {
    //     username: user.username,
    //     password: password
    //   }
    // });
  }
  function editCommentContent(mId,newContent)
  {
      
      
    //axios kütüphanesi npm install axios kodu ile indirilebilir.
   const params=new URLSearchParams();
   params.append("commentId",mId);
   params.append("newcontent",newContent);
   axios.defaults.baseURL="http://localhost:8080";
   axios.put("/comments/updatecomment",params,{auth:{username:localStorage.getItem("username"),password:localStorage.getItem("password")}});
  // axios.put("/messages/editmessagecontent", null, {
  //   headers: {
  //     'Authorization': 'Basic ' + btoa(localStorage.getItem("username") + ':' + localStorage.getItem("password"))
  //   },
  //   params: params
  // });
  fetchPostsOfAGroup();
  fetchComments();
  setShowPopUp(false);
  window.history.go(0);
   
  }

  function addSendMessagePermission(id)
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
          //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
          //qs kullanmayınca post isteklerinde veriler api'ya null gidiyor
         const params=new URLSearchParams();
         params.append("userId",id);
         params.append("groupId",groupId);

         axios.defaults.baseURL="http://localhost:8080";
    const qs=require('qs');
    axios.put("/usergrouppermissions/addsendmessagepermission", 
      params,{auth:{username:localStorage.getItem("username"),password:localStorage.getItem("password")}});
    }
    function removeSendMessagePermission(id)
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
          //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
          //qs kullanmayınca post isteklerinde veriler api'ya null gidiyor
         const params=new URLSearchParams();
         params.append("userId",id);
         params.append("groupId",groupId);

         axios.defaults.baseURL="http://localhost:8080";
    const qs=require('qs');
    axios.put("/usergrouppermissions/removesendmessagepermission", 
      params,{auth:{username:localStorage.getItem("username"),password:localStorage.getItem("password")}});
    }
    function addSendMediaPermission(id)
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
          //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
          //qs kullanmayınca post isteklerinde veriler api'ya null gidiyor
         const params=new URLSearchParams();
         params.append("userId",id);
         params.append("groupId",groupId);

         axios.defaults.baseURL="http://localhost:8080";
    const qs=require('qs');
    axios.put("/usergrouppermissions/addsendmediapermission", 
      params,{auth:{username:localStorage.getItem("username"),password:localStorage.getItem("password")}});
    }
    function removeSendMediaPermission(id)
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
          //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
          //qs kullanmayınca post isteklerinde veriler api'ya null gidiyor
         const params=new URLSearchParams();
         params.append("userId",id);
         params.append("groupId",groupId);

         axios.defaults.baseURL="http://localhost:8080";
    const qs=require('qs');
    axios.put("/usergrouppermissions/removesendmediapermission", 
      params,{auth:{username:localStorage.getItem("username"),password:localStorage.getItem("password")}});
    }
    function readFile(f)
    {
      return new Promise((resolve,reject)=>{
        let reader=new FileReader();
        reader.addEventListener("loadend",e=>resolve(e.target.result))
        reader.addEventListener("error",reject)
        reader.readAsArrayBuffer(f);
      })
    }
    async function getBytes(fi)
    {
      return new Uint8Array(await readFile(fi));
    }
    function sendMediaToAGroup()
        {
            
            
          //axios kütüphanesi npm install axios kodu ile indirilebilir.
          //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
          //qs kullanmayınca post isteklerinde veriler api'ya null gidiyor
         
          // const bytes=getBytes(file);
          
          // const name=file.name;
          // const originalFileName=file.name;
          // const contentType=file.type;
          // const params=new URLSearchParams();
          // params.append("name",name);
          // params.append("originalFileName",originalFileName);
          // params.append("contentType",contentType);
          // params.append("multipartFileBytesToBeUploaded",bytes);
          // params.append("groupId",groupId);

          const formData=new FormData();
          formData.append("multipartFileToBeUploaded",file);
          
          
         axios.defaults.baseURL="http://localhost:8080";
    const qs=require('qs');
    axios.post("/medias/sendmediatoagroup/+"+groupId,
      formData,{

        hearders:{
          "Content-Type":"multipart/form-data"
        },
      auth: {
        username: localStorage.getItem("username"),
        password: localStorage.getItem("password")
      }
    }).then(response=>console.log(response.data));
          fetchUser();
         //window.history.go(0); //bu kod fonksiyonun çalışmasına engel oluyor
         
  }
    useEffect(()=> {

     
    
         fetchUser();
        fetchGroup();
        fetchPostsOfAGroup();
        fetchComments();
        fetchMembers();   
        fetchBlockedUsersOfUser();
        fetchPermissionsOfCurrentUserForAGroup();
        
        
     },[]);

     

     const disdiv={
      display:"flex",
      justifyContent:"center",
      alignItems:"center",
      flexDirection:"column",
      rowGap:"20px"
     }
     const baslik={
      width:"300px",
      textAlign:"center",
      border:"1px solid black",
      fontSize:"2rem"
  }

  function handlenewcomment()
    {
        
        
      //axios kütüphanesi npm install axios kodu ile indirilebilir.
      //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
      //qs kullanmayınca post isteklerinde veriler api'ya null gidiyor
     const params=new URLSearchParams();
     params.append("groupId",groupId);
     params.append("content",newCommentContent);
     if(commentToBeQuoted!=null&&commentToBeQuoted.id!=null)
     params.append("commentIdToBeQuoted",commentToBeQuoted.id);
    if(mediaToBeQuoted!=null&&mediaToBeQuoted.id!=null)
    params.append("mediaIdToBeQuoted",mediaToBeQuoted.id);
     axios.defaults.baseURL="http://localhost:8080";
     axios.post("/comments/createcomment", 
      params
    ,{
      auth: {
        username: localStorage.getItem("username"),
        password: localStorage.getItem("password")
      }
      
    });
      
      fetchPostsOfAGroup();
      fetchComments();
      fetchMembers();
      fetchGroup();
      fetchUser();
      fetchBlockedUsersOfUser();
      setNewCommentContent("");
      setCommentToBeQuoted({content:""});
      setMediaToBeQuoted({content_type:""});
      //window.history.go(0); // bu kod işlemin gerçekleşmemesine sebep oluyor
    
    }
    
    
    
      
    
    
    
   const serverUrl="http://localhost:8088";


  return (
    
        <div style={disdiv}>
          
        <div style={baslik}>{group.name}</div>
        <Button variant='warning' onClick={()=>setShowPopUp2(true)}>See Members</Button>
        <Modal show={showPopUp2} onHide={()=>setShowPopUp2(false)}>
    <Modal.Header>
      <Modal.Title>Members</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      {groupMembers.map(member=>
      <div style={{display:"flex",columnGap:"10px"}}>
        <div>{member.name} {member.surname}</div>
        {_.isEqual(user,group.owner)&&!_.isEqual(member,user)?
        <Button variant='warning' onClick={()=>{setUser3Id(member.id);fetchPermissionsOfAUserForAGroup(member.id);setShowPopUp3(true)}}>Change Permissions</Button>
      :<div></div>
      }
      </div>

)}
      
    </Modal.Body>
    <Modal.Footer>
    
    <Button variant='primary' onClick={()=>{setShowPopUp2(false)}}>Close Pop-Up</Button>
    </Modal.Footer>
    </Modal>
    <Modal show={showPopUp3} onHide={()=>setShowPopUp3(false)}>
    <Modal.Header>
      <Modal.Title>Permissions</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <div>
      {permissionsOfAUserForAGroup.includes("SENDMESSAGE")?
      <label>
        <input type='checkbox' defaultChecked={true} onChange={()=>{removeSendMessagePermission(user3Id);fetchPermissionsOfAUserForAGroup(user3Id)}}>
        </input>
        SEND MESSAGE
      </label>
      :
      <label>
        <input type='checkbox' defaultChecked={false} onChange={()=>{addSendMessagePermission(user3Id);fetchPermissionsOfAUserForAGroup(user3Id)}}>
        </input>
        SEND MESSAGE
      </label>
    }
    <span style={{marginLeft:"10px"}}></span>
    {permissionsOfAUserForAGroup.includes("SENDMEDIA")?
      <label>
        <input type='checkbox' defaultChecked={true} onChange={()=>{removeSendMediaPermission(user3Id);fetchPermissionsOfAUserForAGroup(user3Id)}}>
        </input>
        SEND MEDIA
      </label>
      :
      <label>
        <input type='checkbox' defaultChecked={false} onChange={()=>{addSendMediaPermission(user3Id);fetchPermissionsOfAUserForAGroup(user3Id)}}>
        </input>
        SEND MEDIA
      </label>
    }
      </div>

      
    </Modal.Body>
    <Modal.Footer>
    
    <Button variant='primary' onClick={()=>{fetchPermissionsOfAUserForAGroup(user3Id);setShowPopUp3(false)}}>Close Pop-Up</Button>
    </Modal.Footer>
    </Modal>
        {groupPosts.map(post=>
        <div>
        {post.comment!=null?
        <div>
          {post.comment.quotedComment!=null?
          <div style={{backgroundColor:"yellow"}}>
            <div>Quoted Comment Owner-{post.comment.quotedComment.owner.name} {post.comment.quotedComment.owner.surname}</div>
            <div>Quoted Comment Content- {post.comment.quotedComment.content}</div>
            <div>Quoted Comment Date-{moment(post.comment.quotedComment.commentDate).format("HH:mm DD-MM-yyyy")}</div>
          <div>Edited at-{moment(post.comment.quotedComment.commentEditDate).format("HH:mm DD-MM-yyyy")}</div>
          </div>
          :<div></div>
          }
          {post.comment.quotedMedia!=null?
          <div style={{backgroundColor:"yellow"}}>
             {post.comment.quotedMedia.content_type.includes("image")?
          <div style={{backgroundColor:"yellow"}}>
              
              <img src={`${serverUrl}/${post.comment.quotedMedia.name}`} style={{width:"50px",height:"50px"}}/>
              
            <div>{post.comment.quotedMedia.owner.name} {post.comment.quotedMedia.owner.surname} </div>
            
           
            
          </div>
          :<div></div>
        }
        {post.comment.quotedMedia.content_type.includes("audio")?
          <div style={{backgroundColor:"yellow"}}>
            
              <audio controls>
              <source src={`${serverUrl}/${post.comment.quotedMedia.name}`} type={post.comment.quotedMedia.content_type}/>
              </audio>
              
              <div>{post.comment.quotedMedia.owner.name} {post.comment.quotedMedia.owner.surname} </div>
            
          </div>
          :<div></div>
        }
        {post.comment.quotedMedia.content_type.includes("video")?
          <div style={{backgroundColor:"yellow"}}>
            
              <video controls>
                <source src={`${serverUrl}/${mediaToBeQuoted.name}`} type={mediaToBeQuoted.content_type}/>
              </video>
              
              <div>{post.comment.quotedMedia.owner.name} {post.comment.quotedMedia.owner.surname} </div>
            
          </div>
          :<div></div>
        }
          </div>
          :<div></div>
          }
          <div style={{backgroundColor:"gray"}}>
          <div>Comment Owner-{post.comment.owner.name} {post.comment.owner.surname}</div>
          <div>Comment Content- {post.comment.content} </div>
          <div>Comment Date-{moment(post.comment.commentDate).format("HH:mm DD-MM-yyyy")}</div>
          <div>Edited at-{moment(post.comment.commentEditDate).format("HH:mm DD-MM-yyyy")}</div>
          </div>
          <div>
            <Button onClick={()=>{setCommentToBeQuoted({...post.comment});setMediaToBeQuoted({content_type:""})}}>Quote</Button>
            <Modal show={showPopUp} onHide={()=>setShowPopUp(false)}>
    <Modal.Header>
      <Modal.Title>Edit Message</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <input type='text' id='newcontent' value={commentContentToBeEdited} onChange={(e)=>setCommentContentToBeEdited(e.target.value)}></input>
      
    </Modal.Body>
    <Modal.Footer>
    <Button variant='primary' onClick={()=>{editCommentContent(post.comment.id,commentContentToBeEdited)}}>Edit Comment</Button>
    <Button variant='primary' onClick={()=>{setShowPopUp(false)}}>Close Pop-Up</Button>
    </Modal.Footer>
    </Modal>
            {_.isEqual(user,post.comment.owner)?
            <Button variant='warning' onClick={()=>{setCommentContentToBeEdited(post.comment.content);setShowPopUp(true)}}>Edit</Button>
            :<div></div>
          }
          {_.isEqual(user,post.comment.owner)||user.roles.includes("ADMIN")?
            <Button variant='danger' onClick={()=>{deleteComment(post.comment.id)}}>Delete</Button>
            :<div></div>
          }
          </div>
        </div>
        :<div></div>
        }
        {post.media!=null&&post.media.media_address!=null&&post.media.content_type!=null?
        <div>
          {post.media.content_type.includes("image")?
          <div>
              
              <img src={`${serverUrl}/${post.media.name}`} style={{width:"50px",height:"50px"}}/>
              
            <div>{post.media.owner.name} {post.media.owner.surname} </div>
            <Button variant='danger' onClick={()=>{deleteMedia(post.media.id)}}>Delete</Button>
            <Button variant='primary' onClick={()=>{downloadFile(post.media.name)}}>Download</Button>
            <Button variant='primary' onClick={()=>{setMediaToBeQuoted({...post.media});setCommentToBeQuoted({content:""})}}>Quote</Button>
          </div>
          :<div></div>
        }
        {post.media.content_type.includes("audio")?
          <div>
            
              <audio controls>
              <source src={`${serverUrl}/${post.media.name}`} type={post.media.content_type}/>
              </audio>
              
              <div>{post.media.owner.name} {post.media.owner.surname} </div>
            <Button variant='danger' onClick={()=>{deleteMedia(post.media.id)}}>Delete</Button>
            <Button variant='primary' onClick={()=>{downloadFile(post.media.name)}}>Download</Button>
            <Button variant='primary' onClick={()=>{setMediaToBeQuoted({...post.media});setCommentToBeQuoted({content:""})}}>Quote</Button>
          </div>
          :<div></div>
        }
        {post.media.content_type.includes("video")?
          <div>
            
              <video controls>
                <source src={`${serverUrl}/${post.media.name}`} type={post.media.content_type}/>
              </video>
              
              <div>{post.media.owner.name} {post.media.owner.surname} </div>
            <Button variant='danger' onClick={()=>{deleteMedia(post.media.id)}}>Delete</Button>
            <Button variant='primary' onClick={()=>{downloadFile(post.media.name)}}>Download</Button>
            <Button variant='primary' onClick={()=>{setMediaToBeQuoted({...post.media});setCommentToBeQuoted({content:""})}}>Quote</Button>
          </div>
          :<div></div>
        }
        </div>
        :<div></div>

        }
    
        </div>
    )}
    {commentToBeQuoted.content!=""?
    <div>
      <div>Comment To Be Quoted- {commentToBeQuoted.content}</div>
      <Button onClick={()=>{setCommentToBeQuoted({content:""})}}>Give Up To Quote</Button>
    </div>
    :
    <div></div>
  }
  {mediaToBeQuoted.content_type!=""?
    <div>
      {mediaToBeQuoted.content_type.includes("image")?
          <div style={{backgroundColor:"yellow"}}>
              
              <img src={`${serverUrl}/${mediaToBeQuoted.name}`} style={{width:"50px",height:"50px"}}/>
              
            <div>{mediaToBeQuoted.owner.name} {mediaToBeQuoted.owner.surname} </div>
            
           
            
          </div>
          :<div></div>
        }
        {mediaToBeQuoted.content_type.includes("audio")?
          <div style={{backgroundColor:"yellow"}}>
            
              <audio controls>
              <source src={`${serverUrl}/${mediaToBeQuoted.name}`} type={mediaToBeQuoted.content_type}/>
              </audio>
              
              <div>{mediaToBeQuoted.owner.name} {mediaToBeQuoted.owner.surname} </div>
            
          </div>
          :<div></div>
        }
        {mediaToBeQuoted.content_type.includes("video")?
          <div style={{backgroundColor:"yellow"}}>
            
              <video controls>
                <source src={`${serverUrl}/${mediaToBeQuoted.name}`} type={mediaToBeQuoted.content_type}/>
              </video>
              
              <div>{mediaToBeQuoted.owner.name} {mediaToBeQuoted.owner.surname} </div>
            
          </div>
          :<div></div>
        }
        <Button onClick={()=>{setMediaToBeQuoted({content_type:""})}}>Give Up To Quote</Button>
    </div>
    :
    <div>

    </div>
  }
 
 
  {_.find(group.members,user)&&permissionsOfCurrentUserForAGroup.includes("SENDMESSAGE")?
  <div>
    <input type='text' value={newCommentContent} onChange={(e)=>{setNewCommentContent(e.target.value);console.log(newCommentContent)}}/>
    <Button onClick={()=>handlenewcomment()}>Make New Comment</Button>
    <br></br>
  </div>
  :
  <div>The admins of this group have restricted your ability to send message</div>
  }
  {_.find(group.members,user)&&permissionsOfCurrentUserForAGroup.includes("SENDMEDIA")?
  <div>
    <br></br>
    <input type='file' id='file' onChange={(e)=>{setFile(e.target.files[0])}}/>
    <Button onClick={()=>sendMediaToAGroup()}>Send Media</Button>
  </div>
  :
  <div>The admins of this group have restricted your ability to send message</div>
  }
    
    
  </div>
  
)}