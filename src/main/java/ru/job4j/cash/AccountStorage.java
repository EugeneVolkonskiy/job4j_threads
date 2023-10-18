package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {

    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        if (getById(account.id()).isEmpty()) {
            accounts.put(account.id(), account);
            return true;
        }
        return false;
    }

    public synchronized boolean update(Account account) {
        if (getById(account.id()).isPresent()) {
            accounts.put(account.id(), account);
            return true;
        }
        return false;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        if (getById(fromId).isPresent() && getById(toId).isPresent() && amount > 0) {
            if (getById(fromId).get().amount() >= amount) {
                update(new Account(fromId, getById(fromId).get().amount() - amount));
                update(new Account(toId, getById(toId).get().amount() + amount));
                return true;
            } else {
                throw new IllegalArgumentException("Transfer failed. Not enough money on account");
            }
        }
        return false;
    }
}
