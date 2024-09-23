package com.shtamov.pastebin.extern.repositories;

import com.shtamov.pastebin.domain.models.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepository extends JpaRepository<Text, String> {
}
