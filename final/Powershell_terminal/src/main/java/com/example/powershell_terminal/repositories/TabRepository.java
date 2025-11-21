package com.example.powershell_terminal.repositories;

import com.example.powershell_terminal.model.Tab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TabRepository extends JpaRepository<Tab, Long> {}