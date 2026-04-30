package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Gender;
import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * Pruebas de {@link Registry#registerVoter(Person)} por clases de equivalencia (mínimo 5 cubiertas):
 * <ul>
 *   <li>Entrada nula → {@link RegisterResult#INVALID}</li>
 *   <li>Persona no viva → {@link RegisterResult#DEAD}</li>
 *   <li>Edad fuera de rango permitido ({@code [0, 120]}) → {@link RegisterResult#INVALID_AGE}</li>
 *   <li>Menor de edad ({@code age &lt; 18}) con edad en rango → {@link RegisterResult#UNDERAGE}</li>
 *   <li>Documento ya inscrito válidamente antes → {@link RegisterResult#DUPLICATED}</li>
 *   <li>Votante válido (viva, edad 18–120, documento único) → {@link RegisterResult#VALID}</li>
 * </ul>
 */
public class RegistryTest {

    @Test
    public void shouldReturnInvalidWhenPersonIsNull() {
        // Arrange
        Registry registry = new Registry();

        // Act
        RegisterResult result = registry.registerVoter(null);

        // Assert
        Assert.assertEquals(RegisterResult.INVALID, result);
    }

    @Test
    public void shouldReturnDeadWhenPersonIsNotAlive() {
        // Arrange
        Registry registry = new Registry();
        Person dead = new Person("Carlos", 2, 40, Gender.MALE, false);

        // Act
        RegisterResult result = registry.registerVoter(dead);

        // Assert
        Assert.assertEquals(RegisterResult.DEAD, result);
    }

    @Test
    public void shouldReturnInvalidAgeWhenAgeIsNegative() {
        // Arrange
        Registry registry = new Registry();
        Person invalid = new Person("X", 4, -1, Gender.UNIDENTIFIED, true);

        // Act
        RegisterResult result = registry.registerVoter(invalid);

        // Assert
        Assert.assertEquals(RegisterResult.INVALID_AGE, result);
    }

    @Test
    public void shouldReturnInvalidAgeWhenAgeExceeds120() {
        // Arrange
        Registry registry = new Registry();
        Person invalid = new Person("Y", 5, 121, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(invalid);

        // Assert
        Assert.assertEquals(RegisterResult.INVALID_AGE, result);
    }

    @Test
    public void shouldReturnUnderageWhenAgeIsBelow18() {
        // Arrange
        Registry registry = new Registry();
        Person minor = new Person("Luis", 3, 17, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(minor);

        // Assert
        Assert.assertEquals(RegisterResult.UNDERAGE, result);
    }

    @Test
    public void shouldReturnDuplicatedWhenDocumentAlreadyRegistered() {
        // Arrange
        Registry registry = new Registry();
        Person first = new Person("Ana", 100, 25, Gender.FEMALE, true);
        Person duplicate = new Person("Ana copia", 100, 30, Gender.FEMALE, true);

        // Act
        RegisterResult firstResult = registry.registerVoter(first);
        RegisterResult secondResult = registry.registerVoter(duplicate);

        // Assert
        Assert.assertEquals(RegisterResult.VALID, firstResult);
        Assert.assertEquals(RegisterResult.DUPLICATED, secondResult);
    }

    @Test
    public void shouldReturnValidWhenPersonIsAdultAndDocumentIsUnique() {
        // Arrange
        Registry registry = new Registry();
        Person person = new Person("Ana", 1, 30, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        Assert.assertEquals(RegisterResult.VALID, result);
    }

    @Test
    public void shouldReturnValidWhenAgeIsExactly18() {
        // Arrange
        Registry registry = new Registry();
        Person adult = new Person("M", 200, 18, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(adult);

        // Assert
        Assert.assertEquals(RegisterResult.VALID, result);
    }

    @Test
    public void shouldReturnValidWhenAgeIsExactly120() {
        // Arrange
        Registry registry = new Registry();
        Person elder = new Person("M", 201, 120, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(elder);

        // Assert
        Assert.assertEquals(RegisterResult.VALID, result);
    }
}
