package es.joseluisgs.dam.controllers;

import es.joseluisgs.dam.models.Persona;
import es.joseluisgs.dam.repositories.IPersonasRepository;

import java.sql.SQLException;
import java.util.List;

public class PersonasController {
    private final IPersonasRepository personasRepository;


    public PersonasController(IPersonasRepository personasRepository) {
        this.personasRepository = personasRepository;
    }

    public Persona savePersona(Persona persona) {
        try {
            return personasRepository.save(persona);
        } catch (SQLException e) {
            System.err.println("Error al guardar persona: " + e.getMessage());
        }
        return null;
    }

    public void deleteAll() {
        try {
            personasRepository.deleteAll();
        } catch (SQLException e) {
            System.err.println("Error al borrar todas las personas: " + e.getMessage());
        }
    }

    public List<Persona> findAll() {
        try {
            return personasRepository.findAll();
        } catch (SQLException e) {
            System.err.println("Error al buscar todas las personas: " + e.getMessage());
        }
        return null;
    }

    public Persona update(Persona persona) {
        try {
            return personasRepository.update(persona.getId(), persona);
        } catch (SQLException e) {
            System.err.println("Error al actualizar persona: " + e.getMessage());
        }
        return null;
    }

    public Persona findById(int id) {
        try {
            return personasRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe la persona con id " + id));
        } catch (SQLException e) {
            System.err.println("Error al buscar persona: " + e.getMessage());
        }
        return null;
    }

    public Persona delete(Persona persona) {
        try {
            return personasRepository.delete(persona.getId());
        } catch (SQLException e) {
            System.err.println("Error al borrar persona: " + e.getMessage());
        }
        return null;
    }
}
