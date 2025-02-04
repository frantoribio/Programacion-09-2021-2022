package es.joseluisgs.dam.repositories;

import es.joseluisgs.dam.models.Persona;

import java.sql.SQLException;

public interface IPersonasRepository extends CRUDRepository<Persona, Integer> {
    void deleteAll() throws SQLException;
}
