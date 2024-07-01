package net.lanet.vollmed.domain.sysdb;

import jakarta.persistence.*;

@Entity(name = "SysDb")
@Table(name = "sys_db")
public class SysDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
}
