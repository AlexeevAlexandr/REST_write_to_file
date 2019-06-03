package ua.com.restapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.com.restapplication.exceptionHandling.ExceptionHandling;
import ua.com.restapplication.model.Client;
import ua.com.restapplication.service.MyService;

import java.util.List;

@RestController
@RequestMapping(value = "/clients")
public class MyController {

    private final MyService service;

    @Autowired
    public MyController(MyService service) {
        this.service = service;
    }

    @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public List<Client> getAllClients(){
        return service.getDataFromFile();
    }

    @GetMapping(value = "/{number}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public Client getClientById(@PathVariable String number){

        int id;
        try {
            id = Integer.parseInt(number);
        } catch (Exception e){
            throw new ExceptionHandling("Incorrect number, ID must be integer");
        }

        Client client = service.getClientById(id);
        if (client != null){
            return client;
        } else {
            throw new ExceptionHandling("Client not found");
        }
    }

    @PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public Client createClient(@RequestBody Client client){

        if (client.getName() == null){
            throw new ExceptionHandling("Client name not specified");
        }

        if (client.getEmail() == null){
            throw new ExceptionHandling("Client email not specified");
        }

        List<Client> list = service.getDataFromFile();
        for (Client cl : list) {
            if (cl.getEmail().equals(client.getEmail())){
                throw new ExceptionHandling("Client with email '" + client.getEmail() + "' already exist");
            }
        }
        return service.createClient(client);
    }

    @PutMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public Client updateClient(@RequestBody Client client){

        int id = client.getId();

        if (id == 0) {
            throw new ExceptionHandling("Client ID not specified");
        }

        Client clientId = service.getClientById(id);
        if (clientId == null){
            throw new ExceptionHandling("Client with id '" + client.getId() + "' not found");
        }

        for (Client client2 : service.getDataFromFile()) {
            if (client2.getEmail().equals(client.getEmail()) && client2.getId() != client.getId()){
                throw new ExceptionHandling("Client with email '" + client.getEmail() + "' already exist");
            }
        }

        return service.updateClient(client);
    }

    @DeleteMapping("/{string}")
    public String deleteClient(@PathVariable String string){

        int id;
        try {
            id = Integer.parseInt(string);
        } catch (Exception e){
            throw new ExceptionHandling("Incorrect id '" + string + "', it must be integer");
        }

        if (service.deleteClient(id)){
            return "Client has been deleted";
        } else {
            throw new ExceptionHandling("Client with id '" + id + "' not found");
        }
    }
}