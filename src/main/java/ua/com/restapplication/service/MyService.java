package ua.com.restapplication.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import ua.com.restapplication.model.Client;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MyService implements IService {

    public List<Client> getDataFromFile() {
        try {
            return new CsvToBeanBuilder(new FileReader("src/main/resources/data.csv")).withType(Client.class).build().parse();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Client getClientById(int id){
        List<Client> clients = getDataFromFile();
        for (Client client : clients){
            if (client.getId() == id){
                return client;
            }
        }
        return null;
    }

    public Client createClient(Client client){
        try (CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/data.csv", true))){
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            client.setRegistration_date(date.format(formatter));
            client.setId(getNewID());
            String[] data = {String.valueOf(client.getId()), client.getName(), client.getEmail(), client.getRegistration_date()};
            writer.writeNext(data);
            return client;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client updateClient(Client client){

        List<Client> clientList = getDataFromFile();
        for (Client cl : clientList){
            if (cl.getId() == client.getId()){

                try {
                    String name = client.getName();
                    if (name != null){ cl.setName(name); }
                }catch (Exception ignore){}

                try {
                    String email = client.getEmail();
                    if (email != null){ cl.setEmail(email); }
                }catch (Exception ignore){}

                reWriteDataToFile(clientList);
                return cl;
            }
        }
        return null;
    }

    public boolean deleteClient(int id){

        List<Client> clientList = getDataFromFile();
        for (Client client : clientList) {
            if (client.getId() == id){
                clientList.remove(client);
                return reWriteDataToFile(clientList);
            }
        }
        return false;
    }

    private boolean reWriteDataToFile(List<Client> clientList){
        try (CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/data.csv"))) {
            String[] header = {"id", "name", "email", "registration_date"};
            writer.writeNext(header);
            for (Client client : clientList) {
                String[] data = {String.valueOf(client.getId()), client.getName(), client.getEmail(), client.getRegistration_date()};
                writer.writeNext(data);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getNewID(){
        List<Client> clients = getDataFromFile();
        if (clients.isEmpty()){
            return 1;
        }
        Client cl = clients.get(clients.size()-1);
        return cl.getId() + 1;
    }
}