package com.example.cglprojectv2.service.common;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public class AbstractService<T, R extends JpaRepository<T, Long>> {

    protected R repository;

    public Stream<T> getAll() {
        return repository.findAll().stream();
    }

    public Optional<T> get(Long id) {
        return repository.findById(id);
    }

    public T save(T entity) { //Throw ?
        return repository.saveAndFlush(entity);
    }

    public void remove(T entity) {
        repository.delete(entity);
    }

    public void remove(Long id) {
        repository.deleteById(id);
    }
}