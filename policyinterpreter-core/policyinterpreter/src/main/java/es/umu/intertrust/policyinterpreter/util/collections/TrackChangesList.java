package es.umu.intertrust.policyinterpreter.util.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class TrackChangesList<T> {

    List<T> list;
    List<T> addedElements;
    List<T> updatedElements;
    List<T> removedElements;

    public TrackChangesList() {
        this.list = new ArrayList<T>();
        this.addedElements = new ArrayList<T>();
        this.updatedElements = new ArrayList<T>();
        this.removedElements = new ArrayList<T>();
    }

    public void add(T element) throws AlreadyAddedException, AddingRemovedWarning {
        if (list.contains(element)) {
            throw new AlreadyAddedException("Adding already added element: " + element);
        }
        if (removedElements.contains(element)) {
            removedElements.remove(element);
            list.add(element);
            throw new AddingRemovedWarning("Adding element already set to be removed: " + element);
        } else {
            list.add(element);
            addedElements.add(element);
        }
    }

    public void update(T element) throws NotFoundException {
        if (!list.contains(element)) {
            throw new NotFoundException("Updating nonexistent element: " + element);
        }
        // Removing and adding should be done in case equals is overriden by the element
        list.remove(element);
        list.add(element);
        updatedElements.add(element);
    }

    public void remove(T element) throws NotFoundWarning, RemovingAddedWarning {
        if (!list.contains(element)) {
            throw new NotFoundWarning("Removal of nonexistent element: " + element);
        } else if (addedElements.contains(element)) {
            addedElements.remove(element);
            list.remove(element);
            throw new RemovingAddedWarning("Removing element already set to be added: " + element);
        } else {
            if (updatedElements.contains(element)) {
                updatedElements.remove(element);
            }
            list.remove(element);
            removedElements.add(element);
        }
    }

    public boolean contains(T element) {
        return list.contains(element);
    }

    public boolean hasChanges() {
        return !addedElements.isEmpty() || !updatedElements.isEmpty() || !removedElements.isEmpty();
    }

    public List<T> getElements() {
        return Collections.unmodifiableList(list);
    }

    public List<T> getAddedElements() {
        return Collections.unmodifiableList(addedElements);
    }

    public List<T> getUpdatedElements() {
        return Collections.unmodifiableList(updatedElements);
    }

    public List<T> getRemovedElements() {
        return Collections.unmodifiableList(removedElements);
    }

    public void cleanChanges() {
        addedElements.clear();
        updatedElements.clear();
        removedElements.clear();
    }

}
