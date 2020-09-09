/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var currentPage = "login";
var sessionID = "";

function auth() {
    var msg=document.getElementById("loginmsg");
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    
    if (currentPage == "login") {
        $.ajax({
        url: 'api/account/login',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify({"username":username,"password":password, "id":"null", "session":"null"}),
        success: function( response ){
       // SUCCESS...
	sessionID = response;
        var content = document.getElementById("content");
        content.innerHTML = "";
        populate();
        $('body').css('background', 'transparent');
    },
        error: function( errMsg ){
        // ERROR
	msg.innerHTML = "Login failed!";
    }
    }); 
    } 
    else {
       $.ajax({
        url: 'api/account/register',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify({"username":username,"password":password, "id":"null", "session":"null"}),
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
    
}

function nav() {
    var header=document.getElementById("header");
    var nav=document.getElementById("nav");
    var authBtn=document.getElementById("authBtn");
    if (currentPage == "login") {
        //Goto register
        header.innerHTML = "Register an account";
        nav.innerHTML = "Back to login";
        authBtn.value = "Register";
        currentPage = "Register";
    }
    else {
        //Goto login
        header.innerHTML = "Login";
        nav.innerHTML = "Register a new account";
        authBtn.value = "Login";
        currentPage = "login";
    }
}

function publish(e) {
    var imageInput = document.getElementById("sendImage");
    var description = document.getElementById("description").value;
    var price = document.getElementById("price").value;
    var title = document.getElementById("title").value;
    var fileReader = new FileReader();
    fileReader.readAsDataURL(imageInput.files[0]);
    fileReader.onloadend = function(event)
    {
        var img = event.target.result;
        $.ajax({
        url: 'api/store/publish',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify({"id":"1", "userid":sessionID, "title":title, "description":description, "price":price, "image":img, "sold":"0"}),
        success: function( response ){
       // SUCCESS...
	var content = document.getElementById('content');
        content.innerHTML = "";
        populate();
    },
        error: function( errMsg ){
        // ERROR
	
    }
    }); 
    }
    
}

function populate() {
    var isLoggedIn = authSession();
    var content = document.getElementById('content');
    content.innerHTML = "";
    /*if (isLoggedIn == true ) {
        $("#content").append('<a href="#" id="publish" onclick="publishPage();"> Publish item </a><br><a href="#" id="publish" onclick="historyPage();"> Buy / Sell history</a>');
    }
    else {
        $("#content").append('<a href="#" id="publish" onclick="auth();"> Login </a>');
    }*/
    $.ajax({
        url: 'api/account/session',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify({"username":"","password":"", "id":"null", "session":sessionID}),
        success: function( response ){
       // SUCCESS...
        $("#content").append('<a href="#" id="publish" onclick="publishPage();"> Publish item </a>');
        $("#content").append('<a href="#" id="publish" onclick="historyPage();"> Buy / Sell history</a>');
    },
        error: function( errMsg ){
        // ERROR
	$("#content").append('<a href="#" id="publish" onclick="navLogin();"> Login </a>');
    }
    }); 
    $.getJSON("api/store/retrieve", function(data){
        $.each(data, function (index, value) {
            //(document.getElementById('items').innerHTML = '<div id="item_"><div id="image_"></div><div id="title_"></div><div id="description_"></div></div>';
            //document.write('<div id="item_"><div id="image_"></div><div id="title_"></div><div id="description_"></div></div>');
           
            $("#content").append('<div class="item" id="item_'+value.id+'"><div class="image" id="image_'+value.id+'"></div><div class="title" id="title_'+value.id+'"></div><div class="description" id="description_'+value.id+'"></div><div class="price" id="price_'+value.id+'"></div><div class="buy"><a href="#" id="buy" onclick="buyItem('+value.id+');">Buy item</a></div></div>');
            document.getElementById('title_'+value.id).innerHTML = value.title;
            document.getElementById('description_'+value.id).innerHTML = "Description: "+value.description;
            document.getElementById('price_'+value.id).innerHTML = "Price: "+value.price;
            var appendImage = document.createElement("img");
            appendImage.src = value.image;
            document.getElementById('image_'+value.id).appendChild(appendImage);
        });
    });
}

function publishPage() {
    var content = document.getElementById('content');
    content.innerHTML = "";
    content.innerHTML = '<p>Upload image: </p><input type="file" id="sendImage" accept="image/png, image/jpeg" /> <br/><p>Title of the item: </p><input type="text" id="title" placeholder="Enter title here"><p>Place a description of the item: </p><input type="text" id="description" placeholder="Enter description here"><p>Price: </p><input type="text" id="price" placeholder="Enter price"><input type="submit" id="sell" value="Publish ad" onclick="publish();">';
}

function authSession() {
    if (sessionID == "") {
        return 0;
    }
        
}

function navLogin() {
    var content = document.getElementById('content');
    content.innerHTML = "";
    content.innerHTML = '<div class="loginbox"><a id="loginmsg"></a><img src="avatar.png" class="avatar"><h1 id="header">Login here</h1><p>Username: </p><input type="text" id="username" placeholder="Enter usernamer"><p>Password: </p><input type="password" id="password" placeholder="Enter password"><br><input type="submit" id="authBtn" value="Login" onclick="auth();"><br><a href="#" onclick="nav();" id="nav">Register</a></div>';
}

function buyItem(id) {
    $.ajax({
        url: 'api/store/buy',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify({"id":id,"description":"", "image":"null", "price":"", "title":"", "userid":sessionID, "sold":"0"}),
        success: function( response ){
       // SUCCESS...
        var content = document.getElementById('content');
        content.innerHTML = "";
        populate();
    },
        error: function( errMsg ){
        // ERROR
    }
    });
}

function historyPage() {
    var content = document.getElementById('content');
    content.innerHTML = "";
    $("#content").append('<a href="#" id="publish" onclick="populate();"> Go back </a>');
    $.getJSON("api/store/sold?session=" + sessionID, function(data)
    {
        $("#content").append('<h1>Sale history</h1>');
        $.each(data, function (index, value) {
            //(document.getElementById('items').innerHTML = '<div id="item_"><div id="image_"></div><div id="title_"></div><div id="description_"></div></div>';
            //document.write('<div id="item_"><div id="image_"></div><div id="title_"></div><div id="description_"></div></div>');
           
            $("#content").append('<div class="item" id="item_'+value.id+'"><div class="image" id="image_'+value.id+'"></div><div class="title" id="title_'+value.id+'"></div><div class="description" id="description_'+value.id+'"></div><div class="price" id="price_'+value.id+'"></div></div>');
            document.getElementById('title_'+value.id).innerHTML = value.title;
            document.getElementById('description_'+value.id).innerHTML = "Description: "+value.description;
            document.getElementById('price_'+value.id).innerHTML = "Price: "+value.price;
            var appendImage = document.createElement("img");
            appendImage.src = value.image;
            document.getElementById('image_'+value.id).appendChild(appendImage);
            if (value.sold == "1") {
                document.getElementById('price_'+value.id).innerHTML = "SOLD!";
            }
        });
    });
    $.getJSON("api/store/bought?session=" + sessionID, function(data)
    {
        $("#content").append('<h1>Buy history</h1>');
        $.each(data, function (index, value) {
            //(document.getElementById('items').innerHTML = '<div id="item_"><div id="image_"></div><div id="title_"></div><div id="description_"></div></div>';
            //document.write('<div id="item_"><div id="image_"></div><div id="title_"></div><div id="description_"></div></div>');
           
            $("#content").append('<div class="item" id="item_sold'+value.id+'"><div class="image" id="image_sold'+value.id+'"></div><div class="title" id="title_sold'+value.id+'"></div><div class="description" id="description_sold'+value.id+'"></div><div class="price" id="price_sold'+value.id+'"></div><div class="buy"></div>');
            document.getElementById('title_sold'+value.id).innerHTML = value.title;
            document.getElementById('description_sold'+value.id).innerHTML = "Description: "+value.description;
            document.getElementById('price_sold'+value.id).innerHTML = "Price: "+value.price;
            var appendImage = document.createElement("img");
            appendImage.src = value.image;
            document.getElementById('image_sold'+value.id).appendChild(appendImage);
            if (value.sold == "1") {
                document.getElementById('price_sold'+value.id).innerHTML = '<div id="itemSold">SOLD!</div>';
            }
        });
    });
}

