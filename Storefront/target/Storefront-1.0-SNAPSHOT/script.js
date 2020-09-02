/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function login() {
    var msg=document.getElementById("loginmsg");
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    

$.ajax({
        url: 'api/account/register',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify({"username":username,"password":password}),
        success: function( response ){
       // SUCCESS...
	msg.innerHTML = "Registration success!";
    },
        error: function( errMsg ){
        // ERROR
	msg.innerHTML = "Registration failed!";
    }
    });
}