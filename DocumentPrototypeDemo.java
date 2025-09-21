// DocumentPrototypeDemo.java
import java.util.ArrayList;
import java.util.List;

// Prototype interface
interface DocumentTemplate extends Cloneable {
    DocumentTemplate clone();
    void render();
}

// Simple Style class with a copy method
class Style {
    String font;
    int size;
    String color;

    Style(String font, int size, String color) {
        this.font = font;
        this.size = size;
        this.color = color;
    }

    Style copy() {
        return new Style(font, size, color);
    }

    @Override
    public String toString() {
        return "Style[" + font + ", " + size + ", " + color + "]";
    }
}

// Section class (part of the document) with deep copy
class Section {
    String heading;
    List<String> paragraphs;

    Section(String heading) {
        this.heading = heading;
        this.paragraphs = new ArrayList<>();
    }

    void addParagraph(String p) { paragraphs.add(p); }

    Section copy() {
        Section s = new Section(this.heading);
        s.paragraphs = new ArrayList<>(this.paragraphs); // String is immutable -> shallow copy of list is fine
        return s;
    }

    @Override
    public String toString() {
        return "Section[" + heading + ", paragraphs=" + paragraphs + "]";
    }
}

// Concrete prototype: ArticleTemplate
class ArticleTemplate implements DocumentTemplate {
    String title;
    String author;
    Style style;
    List<Section> sections;

    ArticleTemplate(String title, String author, Style style) {
        this.title = title;
        this.author = author;
        this.style = style;
        this.sections = new ArrayList<>();
    }

    void addSection(Section s) { sections.add(s); }

    @Override
    public ArticleTemplate clone() {
        ArticleTemplate copy = new ArticleTemplate(this.title, this.author, this.style.copy());
        List<Section> newSections = new ArrayList<>();
        for (Section s : this.sections) newSections.add(s.copy());
        copy.sections = newSections;
        return copy;
    }

    @Override
    public void render() {
        System.out.println("=== Article: " + title + " by " + author + " ===");
        System.out.println("Style: " + style);
        for (Section s : sections) {
            System.out.println("  " + s.heading);
            for (String p : s.paragraphs) System.out.println("    " + p);
        }
    }
}

// Another concrete prototype: BrochureTemplate
class BrochureTemplate implements DocumentTemplate {
    String name;
    Style style;
    List<Section> panels;

    BrochureTemplate(String name, Style style) {
        this.name = name;
        this.style = style;
        this.panels = new ArrayList<>();
    }

    void addPanel(Section s) { panels.add(s); }

    @Override
    public BrochureTemplate clone() {
        BrochureTemplate copy = new BrochureTemplate(this.name, this.style.copy());
        List<Section> newPanels = new ArrayList<>();
        for (Section s : this.panels) newPanels.add(s.copy());
        copy.panels = newPanels;
        return copy;
    }

    @Override
    public void render() {
        System.out.println("=== Brochure: " + name + " ===");
        System.out.println("Style: " + style);
        for (Section s : panels) {
            System.out.println(" Panel: " + s.heading);
        }
    }
}

// Client/demo
public class DocumentPrototypeDemo {
    public static void main(String[] args) {
        // Create a base (prototype) article template
        Style baseStyle = new Style("Times New Roman", 12, "black");
        ArticleTemplate articlePrototype = new ArticleTemplate("Tech Trends", "Editorial Team", baseStyle);
        Section intro = new Section("Introduction");
        intro.addParagraph("Welcome to the future of tech.");
        articlePrototype.addSection(intro);
        Section body = new Section("Body");
        body.addParagraph("Deep content goes here.");
        articlePrototype.addSection(body);

        // Clone and customize
        ArticleTemplate article1 = articlePrototype.clone();
        article1.title = "AI in Healthcare";
        article1.author = "Alice";
        article1.style.color = "darkblue"; // modifies only the clone's style (because style was copied)

        // Clone again and customize
        ArticleTemplate article2 = articlePrototype.clone();
        article2.title = "Sustainable Computing";
        article2.author = "Bob";
        article2.sections.get(1).addParagraph("Additional paragraph for Bob's article.");

        // Render prototypes
        System.out.println("Original prototype rendering:");
        articlePrototype.render();
        System.out.println("\nCloned article 1 rendering:");
        article1.render();
        System.out.println("\nCloned article 2 rendering:");
        article2.render();

        // Brochure demo
        BrochureTemplate brochureProto = new BrochureTemplate("Product Launch", new Style("Arial", 14, "white"));
        brochureProto.addPanel(new Section("Front"));
        brochureProto.addPanel(new Section("Back"));

        BrochureTemplate brochureCopy = brochureProto.clone();
        brochureCopy.name = "Product Launch — Regional";
        System.out.println("\nBrochure prototype:");
        brochureProto.render();
        System.out.println("\nBrochure clone:");
        brochureCopy.render();
    }
}
