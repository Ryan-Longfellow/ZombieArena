package me.Visionexe.ZombieArena.Storage.Flatfile;

public class Config {

    protected String label, resourcePath, resourceName, filePath, fileName;

    /**
     * <p>Sets the default information of your configurations file.</p>
     * <p>For full documentation with examples please visit pub.skrypt.net</p>
     * @param label <b>Unique</b> identifier for your configurations
     * @param resourcePath Resource (source) path of the file (Inside your project)
     * @param resourceName Resource (source) name of the file (Inside your project)
     * @param filePath Target path of the file (Inside the /plugins/ directory)
     * @param fileName Target name of the file (Inside the /plugins/ directory)
     */
    public Config(String label, String resourcePath, String resourceName, String filePath, String fileName) {
        this.label = label;
        this.resourcePath = resourcePath;
        this.resourceName = resourceName;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getLabel() {
        return this.label;
    }

}
