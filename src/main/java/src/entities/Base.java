
package src.entities;

import java.time.LocalDateTime;

/**
 *
 * @author Ivan
 */
public abstract class Base {
    private final Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;
    private static Long numeradorId = 0L;

    public Base() {
        this(++numeradorId,false,LocalDateTime.now());
    }

    public Base(Long id,boolean eliminado, LocalDateTime createdAt) {
        this.id = id;
        this.eliminado = eliminado;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public abstract String toString();  
}
