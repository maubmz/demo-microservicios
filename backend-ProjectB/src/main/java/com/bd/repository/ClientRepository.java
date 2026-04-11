package com.bd.repository;

import com.bd.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// En las consultas con notacion @Query se utiliza el nombre de la clase y no el de la tabla
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.purchases p LEFT JOIN FETCH p.products")
    List<Client> findAllWithPurchases();

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.purchases p LEFT JOIN FETCH p.products WHERE c.id = :id")
    Optional<Client> findByIdWithPurchases(@Param("id") Integer id);

    Client save(Client client);

    void deleteById( Integer id);
}
