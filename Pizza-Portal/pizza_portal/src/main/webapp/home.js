

$(function() {

    var cartitem = [];
    var selectedpizza=[];
    var cartsize = 0;
    var $defaultPizza = [];
    var size=[];
    var sauce=[];
    var toppings=[];
    var user = {};

    $("#choosemenu").click(function() {
        $(".welcomePageInfoBox > *").css('display','none');
        $("#choosemenudiv").show();
        $("#menuBar").show();
        generateTable();
    });

    $("#yourown").click(function() {
        $(".welcomePageInfoBox > *").css('display','none');
        $("#yourowndiv").show();
        $("#menuBar").show();
        generateForm();
    });

    $("#homepage").click(function() {
        $(".welcomePageInfoBox > *").css('display','none');
        $("#loginForm > *").css('display','none');
        $("#menuBar").show();
        $("#index-popup").show();
    });

    $("#loginClick").click(function() {
        var email = $("#usr").val();
        var pass = $("#pass").val();
        $.post({
            url: "http://localhost:8080/pizzaDeliverySystem/user/authenticate/"+email+"/password/"+pass +""
        }).then(function(data) {
            if(data.userName!=null){
                $(".welcomePageInfoBox > *").css('display', 'none');
                $("#index-popup").show();
                $("#menuBar").show();
                user = data;
                $("#loggedInUser").html( user.userName );
            }else{
                $("#usr").val("");
                $("#pass").val("");
                alert("login failed");
            }

        });


    });

    function generateUserInfo() {
        var data = [];

        // ajax call
        $.when(

            $.get("http://localhost:8080/pizzaDeliverySystem/user/" + user.emailAddress, function(userinfo) {
                user = userinfo;
            }),


            $.get("http://localhost:8080/pizzaDeliverySystem/userOrder/" + user.emailAddress, function(orders) {

                if(!orders){
                    data = {};
                    console.log("orders");
                    console.log(data);
                }else{
                    data = orders;
                }

            })





        ).then(function() {


            var $userTable = $("#userTable");
            $userTable.empty();
            var elementstoAppend = [];
            var orderlist = [];
            var rowArray = [];
            rowArray[0] = "<td> Email </td><td>" + user.emailAddress + "</td>";
            rowArray[1] = "<td> Phone </td><td>" + user.phoneNumber + "</td>";
            rowArray[2] = "<td> Address </td><td> " + user.street + ", " + user.city + " " + user.state + " " + user.zipCode + "</td>";
            rowArray[3] = "<td> Previous Orders </td><td> All previous order details-</td>";

            for (var i = 0; i < 4; i++) {
                elementstoAppend[i] = "<tr>" + rowArray[i] + "</tr>";
            }
            if(data.length > 0)
            {
                var orderlength = data.length;



            var rowcount = 0;
            for (var i = 0; i < orderlength; i++) {
                rowArray = [];
                rowArray[0] = "<td>" + data[i].orderId + "</td>";
                rowArray[1] = "<td>" + data[i].orderTimeString + "</td>";
                rowArray[2] = "<td>" + data[i].deliveryTimeString + "</td>";
                var pizzacount = 0;
                for (var j = 0; j < data[i].pizzaList.length; j++) {
                    pizzacount = pizzacount + 1;
                }
                rowArray[3] = "<td>" + pizzacount + "</td>";
                var pizzas = data[i].pizzaList;
                //  var cost = 0;
                // for(var j =0;j < pizzas.length ; j++ ){
                //    cost = pizzas[j].cost + cost;
                //}
                //rowArray[4] = "<td>" + cost + "</td>";
                orderlist[++rowcount] = "<tr>" + rowArray.join('') + "</tr>";

            }

            var orderlistheader = "<tr><td>orderID</td><td>OrderTime</td><td>DeliveryTime</td><td>PizzaQty</td></tr>"

            $userTable.html(elementstoAppend.join('') + orderlistheader + orderlist.join(''));
        }else{
                $userTable.html(elementstoAppend.join(''));
            }
        });
    }


    $("#signUpClick").click(function() {
        $(".welcomePageInfoBox > *").css('display','none');
        $("#signUpForm").show();
    });

    $("#signUpSubmit").click(function() {
            var signup={};
            signup.userName = $("#newUser").val();
            signup.password = $("#usrPass").val();
            signup.phoneNumber=$("#usrPhone").val();
            signup.emailAddress =$("#usrEmail").val();
            signup.zipCode = $("#usrZipcode").val();
            signup.street =$("#usrStreet").val();
            signup.city= $("#usrCity").val();
            signup.state=$("#usrState").val();
          //  signUp = { "userName":$("#newUser").val(), "password":$("#usrPass").val(), "phoneNumber":$("#usrPhone").val(),"emailAddress":$("#usrEmail").val(),"zipcode":$("#usrZipcode").val(),"street":$("#usrStreet").val(),"city":$("#usrCity").val(),"state":$("#usrState").val()};

        $.ajax({
            url : "http://localhost:8080/pizzaDeliverySystem/user",
            type: "POST",
            data: JSON.stringify(signup),
            contentType: "application/json; charset=utf-8",
            success    : function(data){
                if(data.userName!=null){
                    $(".welcomePageInfoBox > *").css('display','none');
                    $("#index-popup").show();
                    $("#menuBar").show();
                    user = data;
                    $("#loggedInUser").html( user.userName );
                    generateUserInfo();

                }else{
                    alert("sign up failed");
                }

                console.log("success");
            },
            failure : function(data){
                console.log("Pure jQuery Pure JS object" + data);
            }
        });
    });

    $("#seeProfile").click(function() {
        $(".welcomePageInfoBox > *").css('display','none');
        $("#userProfile").show();
        $("#menuBar").show();
        generateUserInfo();
    });

    $("#showCart").click(function() {
        console.log("selected pizza");
        console.log(selectedpizza);
        $.ajax({
            url : "http://localhost:8080/pizzaDeliverySystem/orderPizza/price",
            type: "POST",
            data: JSON.stringify(selectedpizza),
            contentType: "application/json; charset=utf-8",
            success    : function(data){
                if(data!=null){
                    $(".welcomePageInfoBox > *").css('display','none');
                    $("#displayCart").show();
                    $("#menuBar").show();
                    cartitem = data;
                    generateCartData(data);
                }else{
                    alert("card loading failed");
                }

                console.log("success");
            },
            failure : function(data){
                console.log("Pure jQuery Pure JS object" + data);
            }
        });



    });

    $("#placeOrder").click(function() {
       order={};
       order.pizzaList = cartitem;
       order.user= user;
       order.deliveryAddress = user.street+", "+ user.city +", "+user.state +" "+ user.zipCode;


        // update order/user tables.
        $.ajax({
            url: "http://localhost:8080/pizzaDeliverySystem/orderPizza",
            type: "POST",
            data: JSON.stringify(order),
            contentType: "application/json",
            success    : function(data){

                $(".welcomePageInfoBox > *").css('display','none');
                $("#orderPlaced").show();
                $("#menuBar").show();
                console.log("success");
                cartitem = [];
                selectedpizza = [];
                cartsize = 0;
            },
            failure : function(data){
                console.log("Pure jQuery Pure JS object" + data);
            }
        });

    });

    function generateCartData(cartitem) {
        var $cartHeader = $("#cartHeader");
        var $cartTable = $("#cartTable");
        var $totalCost = $("#totalCost");
        var columns = ["pizza name", "pizza toppings", "pizza sauce", "pizza size", "quantity", "cost"];
        var totalCost = 0;
        var elementstoAppend = [];

        $cartHeader.empty();
        $cartTable.empty();

        elementstoAppend[0] = "<tr>";
        for (var i = 0; i < columns.length; i++) {
            elementstoAppend[i] = "<th>" + columns[i] + "</th>";
        }
        elementstoAppend[elementstoAppend.length] = "</tr>";
        $cartHeader.html(elementstoAppend.join(''));

        elementstoAppend = [];
        for (var i = 0; i < cartitem.length; i++) {
            var rowArray = [];
            rowArray[0] = "<td>"+cartitem[i].pizzaName+"</td>";
            var stoppings = "";
            for (var j = 0; j < cartitem[i].toppingList.length; j++) {
                stoppings += cartitem[i].toppingList[j].toppingName;
                if(j < cartitem[i].toppingList.length-1) stoppings += ",";
            }
            rowArray[1] = "<td>"+stoppings+"</td>";
            rowArray[2] = "<td>"+cartitem[i].sauce.sauceName+"</td>";
            rowArray[3] = "<td>"+cartitem[i].size.sizeName+"</td>";
            rowArray[4] = "<td>"+cartitem[i].quantity+"</td>";
            rowArray[5] = "<td>"+cartitem[i].cost+"</td>";
//            for (var j = 0; j < 6; j++) {
            elementstoAppend[i] = "<tr>" + rowArray.join('') + "</tr>";
//            }
            console.log(elementstoAppend[i]);
            totalCost += cartitem[i].cost;
        }
        $cartTable.html(elementstoAppend.join(''));
        $totalCost.html("$ " + totalCost);
    }
    

    function generateTable() {

        $.ajax({
            url: "http://localhost:8080/pizzaDeliverySystem/defaultPizzas"
        }).then(function(data) {
           console.log(data);
           $data = data;
            $defaultPizza = data;
//            console.log("data = " + $data[0].pizzaId);

            var $tableHeader = $("#tableHeader");
            var $tableBody = $("#tableBody");
            var columns = ["pizza name", "pizza toppings", "Qty","pizza sauce", "pizza size"]; // get column from main table data as the same fuction is used for filterd data also
            var dataCount = 6;
            var columnsCount = 5;
            var elementstoAppend = [];

            // clear previously displayed table data
            $tableHeader.empty();
            $tableBody.empty();

            // adding table headers
            elementstoAppend[0] = "<tr><th></th>";

            for (var i = 1; i <= columnsCount; i++) {
                elementstoAppend[i] = "<th>" + columns[i - 1] + "</th>";
            }

            // adding extra column for show tree button
            var elementstoAppendLastIndex = elementstoAppend.length;
            elementstoAppend[elementstoAppendLastIndex] = "</tr>";
            $tableHeader.html(elementstoAppend.join(''));

            // adding data rows
            elementstoAppend = [];
            for (var i = 0; i < $data.length; i++) {
                // var dataRow = data[i];
                var rowArray = [];
                rowArray[0] = "<td>" + $data[i].pizzaName + "</td>";
                var stoppings = "";
                for (var j = 0; j < $data[i].toppingList.length; j++) {
                    stoppings += $data[i].toppingList[j].toppingName;
                    if(j < $data[i].toppingList.length-1) stoppings += ",";
                }

                rowArray[1] = "<td>" + stoppings + "</td>";
                rowArray[2] = "<td><input id= Qty"+ $data[i].pizzaId+" type='text' value ='1'></td>";
                rowArray[3] = "<td>" + $data[i].sauce.sauceName + "</td>";
                rowArray[4] = "<td>" + $data[i].size.sizeName + "</td>";

                var addToCartButton = "<td><button type='button' class='btn btn-primary addtocart' value =" + $data[i].pizzaId +
                    ">Add To cart</button></td>";
                elementstoAppend[i] = "<tr>" + addToCartButton + rowArray.join('') + "</tr>";
//                console.log(elementstoAppend[i]);
            }
            $tableBody.html(elementstoAppend.join(''));
        });
    }

    $(document).on('click', "#tableBody .addtocart", function(e){
        var k = getObjects($defaultPizza,"pizzaId",$(this).val());
        var Qtyid = "#Qty"+$(this).val();
        k[0].quantity = $(Qtyid).val();
        selectedpizza.push(k[0]);
       console.log(selectedpizza);
       alert("pizza added");

    });


    $(document).on('click', "#CustomPizzaAddBtn", function(e){
        var $sizeId = document.querySelector('input[name="size"]:checked').value;
        var $sauceId = document.querySelector('input[name="sauce"]:checked').value;
        var $toppingsId = [];
        var $Quantity = $("#customPizzaQuantitiy").val();
         console.log("CostomizzaQuantiy " + $Quantity);
        $("input:checkbox[name=topping]:checked").each(function(){
            $toppingsId.push($(this).val());
        });


       // console.log($sizeId);
        //console.log($sauceId);
        //console.log($toppingsId);
        var obj = new Object();
        obj.pizzaId = null;
        obj.pizzaName = "Made by you";
        obj.quantity = $Quantity;
        obj.sauce = {};
        obj.sauce.sauceId = $sauceId;
        obj.sauce.sauceName = getObjects(sauce,"sauceId",$sauceId)[0].sauceName;;
        obj.size = {};
        obj.size.sizeId = $sizeId;
        obj.size.sizeName = getObjects(size,"sizeId",$sizeId)[0].sizeName;
        obj.size.priceFactor = getObjects(size,"sizeId",$sizeId)[0].priceFactor;
        obj.toppingList = [];
        for (var i = 0; i < $toppingsId.length; i++) {
            obj.toppingList[i] = {};
            obj.toppingList[i].toppingId=$toppingsId[i];
            obj.toppingList[i].toppingName=getObjects(toppings,"toppingId",$toppingsId[i])[0].toppingName;
            obj.toppingList[i].price=getObjects(toppings,"toppingId",$toppingsId[i])[0].price;;
        }
       // console.log(obj);
       // console.log(obj.sauce);
       // console.log(obj.size);
       // console.log("toppings make my own" + obj.toppingList);
        for (var i = 0; i < $toppingsId.length; i++) {
            console.log(obj.toppingList[i]);
        }
        selectedpizza.push(obj);
        alert("pizza added");
        console.log(selectedpizza);
    });


    function getObjects(obj, key, val) {
        var objects = [];
        for (var i in obj) {
            if (!obj.hasOwnProperty(i)) continue;
            if (typeof obj[i] == 'object') {
                objects = objects.concat(getObjects(obj[i], key, val));
            } else if (i == key && obj[key] == val) {
                objects.push(obj);
            }
        }
        return objects;
    }

/*// how to get the value of the add to cart button that is clicked?
    $("#addtocartbtn").click(function() {
        $(".welcomePageInfoBox > *").css('display','none');
        $("#shoppay").show();
//        generateShopPayPopup();
    });*/

   function getSize(){

       $.ajax({
           url: "http://localhost:8080/pizzaDeliverySystem/size"
       }).then(function(data) {
           console.log("sizes"+ data);
           sizes = data;
       });

   }


   function getSauce(){

       $.ajax({
           url: "http://localhost:8080/pizzaDeliverySystem/sauce"
       }).then(function(data) {
           console.log("sauces"+ data);
           sauces = data;
       });
   }

   function getTopping(){

       $.ajax({
           url: "http://localhost:8080/pizzaDeliverySystem/toppings"
       }).then(function(data) {
           console.log("sauces"+ data);
           Toppings = data;
       });
   }
    function generateForm() {

        $.when(

            $.get("http://localhost:8080/pizzaDeliverySystem/toppings", function(top) {
                toppings = top;
            }),


            $.get("http://localhost:8080/pizzaDeliverySystem/sauce", function(sau) {
                sauce = sau;
            }),


            $.get("http://localhost:8080/pizzaDeliverySystem/size", function(si) {
                size = si;
            })


        ).then(function() {

           console.log(size);
           console.log(sauce);
           console.log(toppings);

            var $sizeid = $("#size");
            var $sauceid = $("#sauce");
            var $toppingsid = $("#toppings");


            var vegToppings = ["Onions","Green Peppers","Black Olives", "Jalepeno Peppers"];
            var elementstoAppend = [];


            for (var i = 0; i < 3; i++) {
                elementstoAppend[i] = "<input type=\"radio\" value=" + size[i].sizeId + " name=\"size\" checked>" + size[i].sizeName;
            }
            $sizeid.html(elementstoAppend.join(''));

            elementstoAppend = []

            for (var i = 0; i < 5; i++) {
                elementstoAppend[i] = "<input type=\"radio\" value=" + sauce[i].sauceId + " name=\"sauce\" checked>"+ sauce[i].sauceName;
            }
            $sauceid.html(elementstoAppend.join(''));

            elementstoAppend = []


            var k = 0;
            elementstoAppend[k++] = "";
            for (var i = 0; i < 8; i++) {

                elementstoAppend[k++] = "<input type=\"checkbox\" value=" + toppings[i].toppingId + " name=\"topping\">"+ toppings[i].toppingName ;
            }
            elementstoAppend[k++] = "<br>";
            for (var i = 8; i < 16; i++) {

                elementstoAppend[k++] = "<input type=\"checkbox\" value=" + toppings[i].toppingId + " name=\"topping\">"+ toppings[i].toppingName;
            }
            elementstoAppend[k++] = "<br>"
            $toppingsid.html(elementstoAppend.join(''));


        });
    }

});


