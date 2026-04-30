# Historia TDD - Registry (3 ciclos)

Este documento evidencia la aplicacion del enfoque **TDD (Red -> Green -> Refactor)** sobre el caso de uso `registerVoter(Person)` en `Registry`.

## Contexto

Reglas del dominio implementadas:

- Persona `null` -> `INVALID`
- Persona no viva -> `DEAD`
- Edad `< 18` -> `UNDERAGE`
- Edad `< 0` o `> 120` -> `INVALID_AGE`
- Documento repetido -> `DUPLICATED`
- Persona valida y unica -> `VALID`

---

## Ciclo 1 - Registro valido

### RED

Se crea la prueba `shouldReturnValidWhenPersonIsAdultAndDocumentIsUnique`.

- Dada una persona viva, mayor de edad (30) y con documento unico.
- Cuando se intenta registrar.
- Entonces el resultado esperado es `VALID`.

La prueba falla inicialmente porque el caso de uso todavia no devolvia el resultado correcto para el flujo feliz.

### GREEN

Se implementa la logica minima para retornar `VALID` cuando la persona es apta para votar y no existe duplicidad del documento.

### REFACTOR

Se ordena el metodo `registerVoter` y se mantienen reglas claras por prioridad.
Se deja el flujo feliz al final para mejorar legibilidad.

---

## Ciclo 2 - Persona muerta

### RED

Se crea la prueba `shouldReturnDeadWhenPersonIsNotAlive`.

- Dada una persona con `alive = false`.
- Cuando se intenta registrar.
- Entonces el resultado esperado es `DEAD`.

La prueba falla porque el metodo no validaba estado de vida.

### GREEN

Se agrega la validacion:

- Si `!p.isAlive()`, retornar `DEAD`.

Con esto la prueba pasa.

### REFACTOR

Se ubica esta validacion al inicio del metodo, despues de validar `null`, para evitar evaluaciones innecesarias.

---

## Ciclo 3 - Edad y documento duplicado

### RED

Se crean pruebas para los escenarios de equivalencia y borde:

- `shouldReturnUnderageWhenAgeIsBelow18`
- `shouldReturnInvalidAgeWhenAgeIsNegative`
- `shouldReturnInvalidAgeWhenAgeExceeds120`
- `shouldReturnDuplicatedWhenDocumentAlreadyRegistered`
- (bordes validos) `shouldReturnValidWhenAgeIsExactly18` y `shouldReturnValidWhenAgeIsExactly120`

Estas pruebas fallan inicialmente al no existir validaciones de edad ni control de documentos repetidos.

### GREEN

Se implementa la logica minima:

- `age < 0 || age > 120` -> `INVALID_AGE`
- `age < 18` -> `UNDERAGE`
- Se usa un `Set<Integer>` para guardar IDs registrados.
- Si el ID ya existe -> `DUPLICATED`.
- Si no existe, se registra y retorna `VALID`.

### REFACTOR

Se extraen constantes de dominio:

- `MIN_VOTING_AGE = 18`
- `MAX_AGE = 120`

Se mantiene una secuencia clara de validaciones:

1. `null`
2. estado de vida
3. rango de edad
4. mayoria de edad
5. duplicidad
6. alta exitosa

---

## Resultado del enfoque TDD

- Se construyo la logica del dominio guiada por pruebas.
- Cada nueva regla surgio desde una prueba en rojo.
- El codigo final quedo mas mantenible por validaciones ordenadas y constantes de negocio.
- Las pruebas quedaron documentadas con patron AAA y nombres `should...When...()`.
