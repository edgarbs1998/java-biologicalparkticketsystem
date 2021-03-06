package biologicalparkticketsystem.model.document;

import biologicalparkticketsystem.LoggerManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;

/**
 * Abstract class used documents which use dao serialization
 * @param <D> docucment class type
 */
public abstract class DocumentDAOSerialization<D extends IDocument> {
    
    private final String filePath;
    private HashSet<D> list;
    
    public DocumentDAOSerialization(String basePath, String fileName) {
        this.filePath = basePath + fileName;
        
        // Create the path folder if it does not exists
        if (!basePath.equals("")) {
            File file = new File(basePath);
            file.mkdirs();
        }
        
        this.list = new HashSet<>();
        loadAll();
    }
    
    private void loadAll() {
        try {
            try (FileInputStream fileIn = new FileInputStream(this.filePath); ObjectInputStream input = new ObjectInputStream(fileIn)) {
                this.list = (HashSet<D>) input.readObject();
            }
        } catch (IOException | ClassNotFoundException ex) {
            LoggerManager.getInstance().log(ex);
        }
    }
    
    private void saveAll() {
        try {
            try (FileOutputStream fileOut = new FileOutputStream(this.filePath); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(this.list);
            }
        } catch (FileNotFoundException ex) {
            LoggerManager.getInstance().log(ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().log(ex);
        }
    }
    
    /**
     * Method to insert a document to be persisted
     * @param document document instance
     * @return
     */
    public boolean insert(D document) {
        if (this.list.contains(document)) {
            return false;
        }
        this.list.add(document);
        saveAll();
        return true;
    }
    
    /**
     * Method find a document by its unique id
     * @param uid document uid
     * @return document instance
     */
    public D find(String uid) {
        for (D document : this.list) {
            if (document.getUid().equals(uid)) {
                return document;
            }
        }
        return null;
    }
    
    /**
     * Method to return all documents
     * @return a collection of documents
     */
    public Collection<D> selectAll() {
        return this.list;
    }
    
}
