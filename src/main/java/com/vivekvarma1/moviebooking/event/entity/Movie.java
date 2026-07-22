package com.vivekvarma1.moviebooking.event.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Catalog entry for a single movie listing (one language edition).
 * A Movie is screened across many Shows/Screens - that side of the
 * relationship (mappedBy = "movie") gets added once the Show entity
 * exists, so this class doesn't reference a table that isn't built yet.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "movies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_movie_name_language",
                        columnNames = {
                                "movie_name",
                                "language"
                        }
                )
        },
        indexes = {
                @Index(name = "idx_movie_name_language", columnList = "movie_name,language"),
                @Index(name = "idx_movie_status_release_date", columnList = "movie_status,release_date")
        }
)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    @Column(name = "movie_name", nullable = false, length = 255)
    @NotBlank
    @Size(max = 255)
    private String movieName;

    @Column(name = "movie_duration_in_minutes", nullable = false)
    @Min(30)
    @Max(500)
    private int durationInMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false, length = 30)
    @NotNull
    private Language language;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false, length = 30)
    @NotEmpty
    private Set<Genre> genres = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "certificate", nullable = false, length = 20)
    @NotNull
    private Certificate certificate;

    @Enumerated(EnumType.STRING)
    @Column(name = "movie_status", nullable = false, length = 20)
    @NotNull
    private MovieStatus movieStatus;

    @Column(name = "release_date", nullable = false)
    @NotNull
    private LocalDate releaseDate;

    @URL
    @Column(name = "poster_url", length = 1000)
    private String posterUrl;

    @URL
    @Column(name = "trailer_url", length = 1000)
    private String trailerUrl;

    @Size(max = 2000)
    @Column(name = "synopsis", length = 2000)
    private String synopsis;

    /*
     * Optimistic locking. Protects against two concurrent edits (e.g. two
     * admins updating status/synopsis at once) silently overwriting one
     * another - the second commit gets an OptimisticLockException instead
     * of a lost update. Hibernate owns this field entirely; never set it.
     */
    @Version
    @Column(nullable = false)
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /*
     * id-based equality, guarded against Hibernate proxies (this access
     * pattern is required from Hibernate 6 onward - getClass() alone breaks
     * for proxy instances). hashCode() is a constant per class so an
     * entity's bucket in a HashSet/HashMap never shifts when its id moves
     * from null (transient) to a real value (persisted).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> thisEffectiveClass = (this instanceof HibernateProxy)
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        Class<?> otherEffectiveClass = (o instanceof HibernateProxy)
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        if (thisEffectiveClass != otherEffectiveClass) return false;
        Movie movie = (Movie) o;
        return id != null && Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return (this instanceof HibernateProxy)
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

    /*
     * Hand-written and deliberately excludes `genres`: it's a LAZY
     * collection, and an auto-generated toString() (Lombok @ToString or
     * @Data) would touch it on every log statement - throwing
     * LazyInitializationException outside a session, or firing a silent
     * extra query inside one.
     */
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", movieName='" + movieName + '\'' +
                ", durationInMinutes=" + durationInMinutes +
                ", language=" + language +
                ", certificate=" + certificate +
                ", movieStatus=" + movieStatus +
                ", releaseDate=" + releaseDate +
                '}';
    }
}