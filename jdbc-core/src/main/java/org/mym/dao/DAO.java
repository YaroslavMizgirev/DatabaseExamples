package org.mym.dao;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    List<T> getAll();
    Optional<T> get(@NonNull int id);
    List<T> get(Object... params);
    boolean insert(@NonNull T t);
    boolean update(@NonNull T t, Object... params);
    boolean delete(@NonNull T t);
    int count();
}
