package ru.shtamov.s3_service.extern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shtamov.s3_service.domain.Text;

@Repository
public interface TextRepository extends JpaRepository<Text, String> {
}
