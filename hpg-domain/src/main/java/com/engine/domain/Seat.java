package com.engine.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        name = "seat",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_seat_event_row_number",
                columnNames = {
                        "event_id",
                        "row_label",
                        "seat_number"
                }
        )
)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(name = "fk_seat_event"))
    private Event event;

    @Column(name = "row_label", nullable = false, length = 8)
    private String rowLabel;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @Column(name = "allocated", nullable = false)
    private boolean allocated;

    protected Seat() {
        // no-arg constructor
    }

    public Seat(Event event, String rowLabel, int seatNumber) {
        this.event = Objects.requireNonNull(event, "event");
        this.rowLabel = Objects.requireNonNull(rowLabel, "rowLabel");
        if (seatNumber <= 0) {
            throw new IllegalArgumentException("seatNumber must be positive");
        }
        this.seatNumber = seatNumber;
        this.allocated = false;
    }

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = Objects.requireNonNull(event, "event");
    }

    public String getRowLabel() {
        return rowLabel;
    }

    public void setRowLabel(String rowLabel) {
        this.rowLabel = Objects.requireNonNull(rowLabel, "rowLabel");
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        if (seatNumber <= 0) {
            throw new IllegalArgumentException("seatNumber must be positive");
        }
        this.seatNumber = seatNumber;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id == null ? System.identityHashCode(this) : id.hashCode();
    }

    @Override
    public String toString() {
        return "Seat{id=" + id + ", row='" + rowLabel + "', number=" + seatNumber + ", allocated=" + allocated + "}";
    }
}
