/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var currentPage = "login";

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
	msg.innerHTML = "Login success!";
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
        data: JSON.stringify({"id":"1", "userid":"1", "title":title, "description":description, "price":price, "image":img}),
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
    $("#content").append('<a href="#" id="publish" onclick="publishPage();"> Publish item </a>');
    $.getJSON("api/store/retrieve", function(data){
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
        });
    });
}

function publishPage() {
    var content = document.getElementById('content');
    content.innerHTML = "";
    content.innerHTML = '<p>Upload image: </p><input type="file" id="sendImage" accept="image/png, image/jpeg" /> <br/><p>Title of the item: </p><input type="text" id="title" placeholder="Enter title here"><p>Place a description of the item: </p><input type="text" id="description" placeholder="Enter description here"><p>Price: </p><input type="text" id="price" placeholder="Enter price"><input type="submit" id="sell" value="Publish ad" onclick="publish();">';
}