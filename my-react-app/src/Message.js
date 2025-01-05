import React, { useEffect,useState } from 'react';
import {useParams,Link} from "react-router-dom";
import axios from "axios";
import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import 'bootstrap/dist/css/bootstrap.min.css';
import _ from "lodash";
import { Modal,ListGroup, ListGroupItem } from 'react-bootstrap';
import moment from "moment";

export default function Message({messages,setMessages,connectionsOfUser,setConnectionsOfUser,connectionRequests,setConnectionRequests,password,setPassword,user2,setUser2,user,setUser}) {
    const [newMessageContent,setNewMessageContent]=useState("");
    const [messageToBeQuoted,setMessageToBeQuoted]=useState({messageContent:""});
    const [showPopUp,setShowPopUp]=useState(false);
    const [messageContentToBeEdited,setMessageContentToBeEdited]=useState("");
    const[messageToBeEdited,setMessageToBeEdited]=useState({messageContent:""});
    const[messagePosts,setMessagePosts]=useState([]);
    const[file,setFile]=useState(null);
    const{user2Id}=useParams();
    const fetchUser=()=>{
      axios.defaults.baseURL="http://localhost:8080";
      return axios.get("/users/getoneuserbyid",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userId:localStorage.getItem("id")}})
      .then((response)=>{setUser(response.data)});
    }
    const fetchUser2=()=>{
      axios.defaults.baseURL="http://localhost:8080";
      return axios.get("/users/getoneuserbyid",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{userId:user2Id}})
      .then((response)=>{setUser2(response.data)});
    }
    function getMessagesBetweenTwoUsers(){
        axios.defaults.baseURL="http://localhost:8080";
        
        axios.get("/messages/getmessagesbetweentwousers",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{user2Id:user2Id}})
        .then((response)=>{setMessages([...response.data])});
      }
      function getMessagePostsBetweenTwoUsers(){
        axios.defaults.baseURL="http://localhost:8080";
        
        axios.get("/messageposts/getmessagepostsbetweentwousers",{auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{user2Id:user2Id}})
        .then((response)=>{setMessagePosts([...response.data])});
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
      function deleteMedia(mediaId)
      {
        axios.defaults.baseURL="http://localhost:8080";
        axios.delete("/medias/deletemedia",
          {auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{id:mediaId}});
        
        getMessagePostsBetweenTwoUsers();
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
      function handlenewmessage(e)
      {
          e.preventDefault();
          
        //axios kütüphanesi npm install axios kodu ile indirilebilir.
        //qs kullanmak için önce npm i qs yazarak indirmek gerekiyor.
        //qs kullanmayınca post isteklerinde veriler api'ya null gidiyor
       
        axios.defaults.baseURL="http://localhost:8080";
        const qs=require('qs');
        axios.post("/messages/createmessage", 
          qs.stringify({messageReceiverId:user2Id,messageContent:newMessageContent,
            quotedMessageId:messageToBeQuoted.id
          })
        ,{
          auth: {
            username: localStorage.getItem("username"),
            password: localStorage.getItem("password")
          }
        });
        
        
        
        setNewMessageContent("");
        setMessageToBeQuoted({messageContent:""});
        getMessagesBetweenTwoUsers();
        getMessagePostsBetweenTwoUsers();
        //window.history.go(0);
      
      }
      function deleteMessage(mId)
      {
          
          
        //axios kütüphanesi npm install axios kodu ile indirilebilir.
      
       axios.defaults.baseURL="http://localhost:8080";
       axios.delete("/messages/deletemessage",
        {auth: {username: localStorage.getItem("username"),password: localStorage.getItem("password")},params:{messageId:mId}});
      
      getMessagesBetweenTwoUsers();
      getMessagePostsBetweenTwoUsers();
       
       
      }
      function editMessageContent(mId,newContent)
      {
          
          
        //axios kütüphanesi npm install axios kodu ile indirilebilir.
       const params=new URLSearchParams();
       params.append("messageId",mId);
       params.append("newMessageContent",newContent);
       axios.defaults.baseURL="http://localhost:8080";
       axios.put("/messages/editmessagecontent",params,{auth:{username:localStorage.getItem("username"),password:localStorage.getItem("password")}});
      // axios.put("/messages/editmessagecontent", null, {
      //   headers: {
      //     'Authorization': 'Basic ' + btoa(localStorage.getItem("username") + ':' + localStorage.getItem("password"))
      //   },
      //   params: params
      // });
      
      getMessagesBetweenTwoUsers();
      getMessagePostsBetweenTwoUsers();
      setShowPopUp(false);
      window.history.go(0);
       
      }
      function sendMediaAsMessagePost()
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
    axios.post("/medias/sendmediaasamessagepost/+"+user2Id,
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
          getMessagePostsBetweenTwoUsers();
         //window.history.go(0); //bu kod fonksiyonun çalışmasına engel oluyor
         
  }
    useEffect(() => {
        if(localStorage.getItem("id")!=null)
            {
              
                  fetchUser();
                
              fetchUser2();
              getMessagesBetweenTwoUsers();
              getMessagePostsBetweenTwoUsers();
            }

    }, [])
    
   const serverUrl="http://localhost:8088";
    return (
    <div style={{overflow:"scroll",border:"1px solid black", width:"900px",height:"auto",marginLeft:"50px",marginTop:"50px"}}>
        {messagePosts.map(m => { 
            
               return(
                <div>
                  {m.message!=null?
                  <div>
                    {m.message.quotedMessage!=null?
                    <div style={{backgroundColor:"yellow"}}>
                      <div>Quoted Message Sender- {m.message.quotedMessage.messageSender.name} {m.message.quotedMessage.messageSender.surname}</div>
                      <div>Quoted Message Content- {m.message.quotedMessage.messageContent}</div>
                      
                    </div>
                    :<div></div>
                  }
                  <div style={{backgroundColor:"gray"}}>
                  <div>Message Sender- {m.message.messageSender.name} {m.message.messageSender.surname}</div>
                      <div>Message Content- {m.message.messageContent}</div>
                      <div>Message Date- {moment(m.date).format("HH:mm DD-MM-yyyy")}</div>
                      <Button onClick={()=>{setMessageToBeQuoted({...m.message})}}>Quote</Button>
                      {_.isEqual(m.message.messageSender,user)?
                    <div style={{display:'flex',columnGap:"10px"}}>
                    <Button variant="warning" onClick={()=>{setShowPopUp(true)}}>Edit</Button>
                    <Button variant="danger" onClick={()=>{deleteMessage(m.message.id)}}>Delete</Button>
                    <Modal show={showPopUp} onHide={()=>setShowPopUp(false)}>
    <Modal.Header>
      <Modal.Title>Edit Message</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <input type='text' id='newmessageContent' onChange={(e)=>setMessageContentToBeEdited(e.target.value)}></input>
      
    </Modal.Body>
    <Modal.Footer>
    <Button variant='primary' onClick={()=>{editMessageContent(m.message.id,messageContentToBeEdited)}}>Edit Message</Button>
    <Button variant='primary' onClick={()=>{setShowPopUp(false)}}>Close Pop-Up</Button>
    </Modal.Footer>
    </Modal>
                    </div>:
                    <div></div>
                }
                  </div>
                  </div>
                  :<div></div>
                }
                {m.media!=null&&m.media.media_address!=null&&m.media.content_type!=null?
        <div>
          {m.media.content_type.includes("image")?
          <div>
            
              <img src={`${serverUrl}/${m.media.name}`} style={{width:"50px",height:"50px"}}/>
              
            
            <Button variant='danger' onClick={()=>{deleteMedia(m.media.id)}}>Delete</Button>
            <Button variant='primary' onClick={()=>{downloadFile(m.media.name)}}>Download</Button>
          </div>
          :<div></div>
        }
        {m.media.content_type.includes("audio")?
          <div>
            
              <audio controls>
                <source src={`${serverUrl}/${m.media.name}`} type={m.media.content_type}/>
              </audio>
              
            
            <Button variant='danger' onClick={()=>{deleteMedia(m.media.id)}}>Delete</Button>
            <Button variant='primary' onClick={()=>{downloadFile(m.media.name)}}>Download</Button>
          </div>
          :<div></div>
        }
        {m.media.content_type.includes("video")?
          <div>
            
              <video controls>
                <source src={`${serverUrl}/${m.media.name}`} type={m.media.content_type}/>
              </video>
              
            
            <Button variant='danger' onClick={()=>{deleteMedia(m.media.id)}}>Delete</Button>
            <Button variant='primary' onClick={()=>{downloadFile(m.media.name)}}>Download</Button>
          </div>
          :<div></div>
        }
        </div>
        :<div></div>

        }

                
                
                </div>
            );
               

           
})}
{messageToBeQuoted!=null && messageToBeQuoted.messageContent.length!=0? 
           <div>
           <ListGroup>
           <ListGroupItem>{messageToBeQuoted.messageContent}</ListGroupItem>
           </ListGroup>
           
           <Button variant="info" sync="true" onClick={()=>
             setMessageToBeQuoted({messageContent:""})}>Give up to quote message </Button>
           </div>
           :<div></div>}
            
             
                <Form sync="true" onSubmit={(e)=>handlenewmessage(e)}>
                {/* <Form action='http://localhost:8083/users/enteruser' method='post'> */}
        
                
                
              <Form.Group as={Row} className="mb-3" controlId="formHorizontalEmail">
                <div style={{width:"auto"}}>
                  New Message
                </div>
                <div>
                  <Form.Control type="text" placeholder="Message" name="content" autoComplete="off" value={newMessageContent} onChange={(e)=>setNewMessageContent(e.target.value)}/>
                </div>
              </Form.Group>
        
              
              
             
              
        
              <Form.Group as={Row} className="mb-3">
                <Col sm={{ span: 10, offset: 2 }}>
                   <Button type="submit" >Send New Message</Button> 
                </Col>
              </Form.Group>
            </Form>
            <div>
    <br></br>
    <input type='file' id='file' onChange={(e)=>{setFile(e.target.files[0])}}/>
    <Button onClick={()=>sendMediaAsMessagePost()}>Send Media</Button>
  </div>
</div>)}
