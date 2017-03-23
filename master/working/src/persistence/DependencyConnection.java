package persistence;


import domain.Searchable;

/**
 * Allows the formation of arbitrary dependencies between nodes in a graph of StatementNode
 * objects.
 *
 * @author Marcello De Bernardi
 */
public class DependencyConnection {
    private Directionality role;
    private DependencyConnection pair;
    private StatementNode connection;
    private Searchable host;

    /**
     * Create the first of the two DependencyConnection objects. Its role is
     * set in this constructor. Call pair() on this DependencyConnection to
     * receive its entangled pair.
     *
     * @param role transmitter, receiver or bidirectional
     */
    public DependencyConnection(Directionality role) {
        this.role = role;
    }

    private DependencyConnection(DependencyConnection pair) {
        this.pair = pair;

        if (pair.role == Directionality.TRANSMITTER) role = Directionality.RECEIVER;
        else if (pair.role == Directionality.RECEIVER) role = Directionality.TRANSMITTER;
        else role = Directionality.BIDIRECTIONAL;
    }

    public DependencyConnection pair() {
        if (pair == null) pair = new DependencyConnection(this);
        return pair;
    }

    public DependencyConnection host(Searchable host) {
        this.host = host;
        return this;
    }

    /**
     * Performs this pair's end of the communication handshake. Call this method once
     * when the StatementNode is being created, passing the node to the method as
     * argument. The StatementNode does not need to retain a reference to the
     * DependencyConnection.
     *
     * @param node the node host to this transmitter/receiver
     */
    public void transmit(StatementNode node) {
        if (role == Directionality.TRANSMITTER || role == Directionality.BIDIRECTIONAL) {
            if (connection != null) {
                connection.addDependency(node);
                node.addDependent(connection);
            }
            else pair.connection = node;
        }
        if (role == Directionality.RECEIVER || role == Directionality.BIDIRECTIONAL) {
            if (connection != null) {
                node.addDependency(connection);
                connection.addDependent(node);
            }
            else pair.connection = node;
        }
    }


    public Searchable getHost() {
        if (host == null) throw new DependencyException("Dependency connection has no host.");
        return host;
    }

    public Directionality directionality() {
        return this.role;
    }


    public enum Directionality {
        TRANSMITTER, RECEIVER, BIDIRECTIONAL
    }
}