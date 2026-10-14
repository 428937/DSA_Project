# DSA_Project: Real Estate Listing Application

A comprehensive Data Structures and Algorithms project implementing a real estate listing application with advanced searching, filtering, sorting, and management features using various data structures learned in DSA course.

## Project Overview

This application demonstrates the practical implementation of fundamental data structures and algorithms in a real-world scenario. Users can manage property listings, search, filter, sort, and maintain a favorites list.

## Features

- **Property Management**: Add, update, delete, and view property listings
- **Advanced Search**: Search properties by name, location, and price range.
- **Filtering**: Filter properties by property type, location, and price range
- **Sorting**: Sort properties by price (ascending/descending), area, price-to-area ratio
- **Favorites System**: Add/remove properties to/from favorites list with persistent storage
- **Statistics Dashboard**: View key metrics and analytics about listings
- **User-Friendly GUI**: Built with Java Swing for intuitive interaction
- **Data Persistence**: Save and load listings from file storage

## Data Structures Used

1. **Linked List**: Core storage for property listings with dynamic insertion/deletion
2. **Queue**: Used for recent activity tracking and processing property requests
3. **Stack**: Undo/Redo functionality for operations
4. **Binary Search Tree**: Efficient searching and sorting of properties
5. **Hash-based Collections**: For quick property lookup by ID

## Algorithms Implemented

1. **Linear Search**: Search properties by name
2. **Binary Search**: Quick search on sorted data
3. **Bubble Sort**: Basic sorting demonstration
4. **Quick Sort**: Efficient sorting for large datasets
5. **Merge Sort**: Stable sorting for property listings
6. **Tree Traversal**: In-order, Pre-order, Post-order traversals for BST

## Project Structure

```
DSA_Project/
├── src/
│   ├── models/
│   │   ├── Emlak.java                 # Property model class
│   │   ├── BaglıListe.java            # Linked list implementation
│   │   ├── Kuyruk.java                # Queue implementation
│   │   ├── Yıgın.java                 # Stack implementation
│   │   ├── IkiliAramaAgaci.java       # Binary search tree implementation
│   │   └── AramaVeSiralamaAlg.java    # Search and sort algorithms
│   ├── ui/
│   │   ├── AnaEkran.java              # Main window GUI
│   │   ├── IlanEkleDialog.java        # Add property dialog
│   │   ├── IlanGuncelleDialog.java    # Update property dialog
│   │   ├── AramaFiltrelePanel.java    # Search and filter panel
│   │   └── FavoriListesiPanel.java    # Favorites panel
│   ├── yonetici/
│   │   ├── EmlakYoneticisi.java       # Application manager
│   │   ├── VeriYukleme.java           # Data loading/saving
│   │   └── GeriyeAlYineleSistemi.java # Undo/Redo system
│   └── Main.java                       # Application entry point
├── data/
│   └── ilanlar.txt                    # Persistent data storage
├── README.md
├
   ```bash
   git clone https://github.com/428937/DSA_Project.git
   cd DSA_Project
   ```
3. Compile the project:
   ```bash
   javac -d bin src/**/*.java
   ```
4. Run the application:
   ```bash
   java -cp bin Main
   ```

## Usage Guide

### Adding Properties
1. Click "Yeni İlan Ekle" button
2. Fill in property details (name, location, price, area, type)
3. Click "Kaydet" to save

### Searching Properties
- Use the search bar to find properties by name
- Enter search term and click "Ara"

### Filtering Properties
- Select filters (property type, location, price range)
- Click "Filtrele" to apply filters

### Sorting Properties
- Click on column headers to sort by that attribute
- Available sorts: By Price, By Area, By Price-to-Area Ratio

### Managing Favorites
- Click heart icon on any property to add/remove from favorites
- View all favorites in the "Favoriler" tab

### Undo/Redo
- Use Ctrl+Z for undo, Ctrl+Y for redo operations

## Technical Details

### Time Complexity Analysis

| Operation | Data Structure | Complexity |
|-----------|----------------|------------|
| Add Property | Linked List | O(1) |
| Delete Property | Linked List | O(n) |
| Search (Linear) | Linked List | O(n) |
| Search (Binary) | BST | O(log n) |
| Sorted Display | Quick Sort | O(n log n) |
| Favorites Storage | Queue | O(1) amortized |

### Algorithms Features

- **Binary Search Tree** automatically maintains sorted order
- **Quick Sort** used for large datasets with pivot optimization
- **Merge Sort** ensures stability for multi-key sorting
- **Stack-based Undo**: Maintains operation history efficiently

## Learning Outcomes

This project demonstrates:
- Practical implementation of fundamental data structures
- Algorithm selection based on use case requirements
- GUI development with Java Swing
- File I/O and data persistence
- Object-oriented design principles
- Big-O notation and complexity analysis

## Course Context

Developed as a final project for Data Structures and Algorithms course, implementing concepts including:
- Linked lists and dynamic data structures
- Queue operations and applications
- Stack operations and applications
- Binary Search Trees and tree traversal
- Fundamental search and sorting algorithms
- Algorithm complexity and optimization