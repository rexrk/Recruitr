package com.raman.recruitr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_manager_id", unique = true)
    private AppUser accountManager;  // for secutiry

//    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)   //maybe add multiple admins for one orgnztion
//    private Set<AppUser> accountManagers = new HashSet<>();

    private String companyName;
    private String address;
    private String city;
    private String logo;

    private String email;
    private String website;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany
    @JoinTable(
            name = "vendor_client",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "vendor_id")
    )
    private Set<Organization> vendors = new HashSet<>();

    @ManyToMany(mappedBy = "vendors")
    private Set<Organization> clients = new HashSet<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = Status.ACTIVE;
    }

    public enum Status {
        ACTIVE, INACTIVE
    }
}