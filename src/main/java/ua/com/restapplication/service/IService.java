package ua.com.restapplication.service;

import ua.com.restapplication.model.Client;

import java.util.List;


public interface IService {

    List<Client> getDataFromFile();

    Client getClientById(int id);

    Client createClient(Client client);

    Client updateClient(Client client);

    boolean deleteClient(int id);
}
