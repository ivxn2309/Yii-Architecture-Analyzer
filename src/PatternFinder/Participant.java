/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * A participant is an object that take part in the structure
 * of any pattern.
 */

package PatternFinder;

import java.util.Objects;

public class Participant {
    
    public String name;
    public String role;
    public String path;

    public Participant(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public Participant(String name, String role, String path) {
        this.name = name;
        this.role = role;
        this.path = path;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Objects.hashCode(this.role);
        hash = 79 * hash + Objects.hashCode(this.path);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Participant other = (Participant) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
