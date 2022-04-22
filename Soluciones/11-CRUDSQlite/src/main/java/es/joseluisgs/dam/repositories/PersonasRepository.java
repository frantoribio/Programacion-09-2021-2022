package es.joseluisgs.dam.repositories;

import es.joseluisgs.dam.managers.DataBaseManager;
import es.joseluisgs.dam.models.Persona;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PersonasRepository implements IPersonasRepository {
    DataBaseManager db = DataBaseManager.getInstance();

    @Override
    public List<Persona> findAll() throws SQLException {
        // Buscamos todos en la BD.
        String sql = "SELECT * FROM personas";
        db.open();
        var res = db.select(sql).orElseThrow(() -> new SQLException("Error al obtener las personas."));
        // Creamos una lista de personas.
        var personas = new ArrayList<Persona>();
        // Recorremos el resultado.
        while (res.next()) {
            // Creamos una persona.
            var persona = Persona.builder()
                    .id(res.getInt("id"))
                    .uuid(UUID.fromString(res.getString("uuid")))
                    .nombre(res.getString("nombre"))
                    .edad(res.getInt("edad"))
                    .createdAt(LocalDate.parse(res.getString("createdAt"))).build();
            personas.add(persona);
        }
        db.close();
        return personas;
    }

    @Override
    public Optional<Persona> findById(Integer id) throws SQLException {
        var sql = "SELECT * FROM personas WHERE id = ?";
        db.open();
        var res = db.select(sql, id).orElseThrow(() -> new SQLException("Error al obtener la persona con id: " + id));
        if (res.next()) {
            var persona = Persona.builder()
                    .id(res.getInt("id"))
                    .uuid(UUID.fromString(res.getString("uuid")))
                    .nombre(res.getString("nombre"))
                    .edad(res.getInt("edad"))
                    .createdAt(LocalDate.parse(res.getString("createdAt")))
                    .build();
            db.close();
            return Optional.of(persona);
        }
        db.close();
        return Optional.empty();
    }

    @Override
    public Persona save(Persona persona) throws SQLException {
        // Salvamos en la BBDD. // si no ponemos los campos, hay que poner eb values null, como priemr campo por el id
        String sql = "INSERT INTO personas (uuid, nombre, edad, createdAt) VALUES (?, ?, ?, ?)";
        db.open();
        var res = db.insert(sql, persona.getUuid(), persona.getNombre(), persona.getEdad(), persona.getCreatedAt())
                .orElseThrow(() -> new SQLException("Error al insertar la persona."));
        // Le cogemos el id que nos ha dado la BBDD.
        if (res.next()) {
            persona.setId(res.getInt(1));
            db.close();
        } else {
            db.close();
            throw new SQLException("Error al obtener el id de la persona.");
        }
        return persona;
    }

    @Override
    public Persona update(Integer id, Persona persona) throws SQLException {
        var sql = "UPDATE personas SET uuid = ?, nombre = ?, edad = ?, createdAt = ? WHERE id = ?";
        db.open();
        var res = db.update(sql, persona.getUuid(), persona.getNombre(), persona.getEdad(), persona.getCreatedAt(), id);
        db.close();
        if (res > 0) {
            return persona;
        } else {
            throw new SQLException("Error al actualizar la persona con id: " + id);
        }
    }

    @Override
    public Persona delete(Integer id) throws SQLException {
        var persona = findById(id).orElseThrow(() -> new SQLException("Error al borrar la persona con id: " + id));
        var sql = "DELETE FROM personas WHERE id = ?";
        db.open();
        var res = db.delete(sql, id);
        db.close();
        if (res > 0) {
            return persona;
        } else {
            throw new SQLException("Error al borrar la persona con id: " + id);
        }
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM personas";
        db.open();
        db.delete(sql);
        db.close();
    }
}
