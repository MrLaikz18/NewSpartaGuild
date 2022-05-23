package fr.mrlaikz.spartaguild.objects;

import java.util.UUID;

public class Economy {

    private UUID uuid;
    private double balance;

    public Economy(UUID uuid, double bal) {
        this.uuid = uuid;
        this.balance = bal;
    }

    //GETTERS
    public double getBalance() {
        return balance;
    }

    public UUID getUUID() {
        return uuid;
    }

    //SETTERS
    public void deposit(double amount) {
        this.balance+=amount;
    }

    public void withdraw(double amount) {
        this.balance-=amount;
    }

    public void clear() {
        this.balance = 0;
    }

    public void setBalance(double amount) {
        this.balance = amount;
    }


}
