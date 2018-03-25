package com.restapi.controller;

import com.restapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pizzaDeliverySystem")
public class PizzaDeliveryController {

    @Autowired
    PizzaDeliveryService pizzaDeliveryService;

    @GetMapping("/defaultPizzas")
    public ResponseEntity<List<Pizza>> getDefaultPizza() {
        System.out.println("Default Pizzas");
        List<Pizza> defaultPizzas = pizzaDeliveryService.getDefaultPizza();
        if (defaultPizzas.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Pizza>>(defaultPizzas, HttpStatus.OK);
    }

    @GetMapping("/toppings")
    public ResponseEntity<List<Topping>> getToppings() {
        System.out.println("Toppings");
        List<Topping> toppings = pizzaDeliveryService.getToppings();
        if (toppings.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Topping>>(toppings, HttpStatus.OK);
    }

    @GetMapping("/sauce")
    public ResponseEntity<List<Sauce>> getSauce() {
        System.out.println("Sauces");
        List<Sauce> sauces = pizzaDeliveryService.getSauce();
        if (sauces.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Sauce>>(sauces, HttpStatus.OK);
    }

    @GetMapping("/size")
    public ResponseEntity<List<Size>> getSize() {
        System.out.println("Sizes");
        List<Size> sizes = pizzaDeliveryService.getSize();
        if (sizes.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Size>>(sizes, HttpStatus.OK);
    }

    @GetMapping("/userOrder/{email:.+}")
    public ResponseEntity userOrder(@PathVariable String email){
        System.out.println("Order Pizza_old");
        List<Order> userOrders = pizzaDeliveryService.userOrders(email);
        if (userOrders.isEmpty()) {
            return new ResponseEntity(userOrders,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Order>>(userOrders, HttpStatus.OK);

    }

    //USER RELATED -- AUTHENTICATION AND DETAILS
    @GetMapping("/user/{emailAddress:.+}")
    public ResponseEntity<User> getUserDetails(@PathVariable String emailAddress) {
        System.out.println("Get User Details");
        User user = pizzaDeliveryService.getUserDetails(emailAddress);
        if (user==null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> postUserDetails(@RequestBody User user) {
        System.out.println("Post User Details");
        User userObj=pizzaDeliveryService.createUser(user);
        return new ResponseEntity<User>(userObj, HttpStatus.OK);
    }

    //TO BE DONE
    @PostMapping("/user/authenticate/{emailAddress}/password/{password}")
    public ResponseEntity<User> autheticateUser(@PathVariable String emailAddress,@PathVariable String password) {
        System.out.println("Authenticate User");
        User user = pizzaDeliveryService.authenticateUser(emailAddress, password);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    //save user details
    //save placed order -- default :placeOrderForDefault
    // and make your own -- placeOrder
    //show prices at checkout

    @PostMapping("/orderPizza/price")
    public ResponseEntity<List<Pizza>> getPrice(@RequestBody List<Pizza> pizzaList) {
        System.out.println("Pizza price");


        for (Pizza pizza: pizzaList) {
            int pizzaSizePrice = pizza.getSize().getPriceFactor();
            int quantity = pizza.getQuantity();
            int toppingSize = pizza.getToppingList().size();
            int cost = (pizzaSizePrice*quantity)+toppingSize;
            pizza.setCost(cost);
        }

        return new ResponseEntity<List<Pizza>>(pizzaList, HttpStatus.OK);
    }

    @PostMapping("/orderPizza")
    public ResponseEntity<String> orderedPizza(@RequestBody Order order) {
        System.out.println("Ordered Pizza");

        int orderId = pizzaDeliveryService.createOrder(order.getDeliveryAddress(), order.getUser().getUserId());
        for (Pizza pizza: order.getPizzaList()) {
            if(pizza.getPizzaId() > 0) {
                pizzaDeliveryService.placeOrderForDefault(pizza.getPizzaId(), pizza.getQuantity(), orderId, pizza.getCost());
            }else {
                pizzaDeliveryService.placeOrder(pizza.getQuantity(),orderId, pizza.getCost(), pizza.getSauce().getSauceId(), pizza.getSize().getSizeId(), pizza.getToppingList());
            }
        }

        return new ResponseEntity<String>("Ordered", HttpStatus.OK);
    }
}
