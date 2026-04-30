package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;

import java.util.HashSet;
import java.util.Set;

public class Registry {

    private static final int MIN_VOTING_AGE = 18;
    private static final int MAX_AGE = 120;

    private final Set<Integer> registeredIds = new HashSet<>();

    public RegisterResult registerVoter(Person p) {
        if (p == null) {
            return RegisterResult.INVALID;
        }
        if (!p.isAlive()) {
            return RegisterResult.DEAD;
        }
        int age = p.getAge();
        if (age < 0 || age > MAX_AGE) {
            return RegisterResult.INVALID_AGE;
        }
        if (age < MIN_VOTING_AGE) {
            return RegisterResult.UNDERAGE;
        }
        int id = p.getId();
        if (registeredIds.contains(id)) {
            return RegisterResult.DUPLICATED;
        }
        registeredIds.add(id);
        return RegisterResult.VALID;
    }
}
