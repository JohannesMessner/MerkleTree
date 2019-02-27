# MerkleTree
Command-line based implementation of a Merkle-Tree data structure using java.

This application consists of two parts:
- The underlying Merkle-Tree data structure
- The shell, acting as a text-based UI

The Merkle-Tree itself is capable of handling generic datatypes as values.
It can store values, calculate hashes (this implementation does not use a real cryptographic hashing-mechanism) and check itself for consistency.

The shell can create, delete and validate Merkle-Trees and provides two example-classes ('Cuboid' and 'Cylinder') that can be stored in the tree.


--- HOW TO USE ---

The shell can run in three different modes:
- Start-mode: Only active after launching the application
- Build-mode: In build-mode you can create new trees, push values to the tree and get a visual representation of the tree
- Check-mode: In check-mode you can create new trees with a given root-hash, set values and hashes manually for every node in the tree and check the tree for consistency.

A detailed instuction on how to use the application can be obtained by entering 'help' in the command-line at any point after launching the program.
