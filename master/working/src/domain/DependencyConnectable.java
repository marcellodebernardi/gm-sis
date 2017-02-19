package domain;

import persistence.DependencyConnection;

import java.util.List;

/**
 * A DependencyConnectable object can have arbitrary foreign-primary key dependencies
 * implemented with DependencyConnection pairs.
 *
 * @author Marcello De Bernardi
 */
public interface DependencyConnectable {
    List<DependencyConnection> getDependencies();
}
