/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 *222648767
 * @author smitm
 */

public class StudentRecordServer
{
    private ServerSocket listener;
    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private ArrayList<Student> studentList = new ArrayList<>();

    public StudentRecordServer()
    {
        try
        {
            listener = new ServerSocket(6666);
            System.out.println("Server is listening on port 6666");

            client = listener.accept();
            System.out.println("Client connected.");

            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();

            in = new ObjectInputStream(client.getInputStream());

            processClient();
        }
        catch (IOException ex)
        {
            System.out.println("Server error: " + ex.getMessage());
        }
        finally
        {
            closeConnection();
        }
    }

    private void processClient()
    {
        boolean running = true;

        while (running)
        {
            try
            {
                String request = (String) in.readObject();

                switch (request)
                {
                    case "ADD":
                        Student student = (Student) in.readObject();
                        studentList.add(student);

                        out.writeObject("Student added successfully.");
                        out.flush();

                        System.out.println("Added: " + student);
                        break;

                    case "RETRIEVE":
                        out.writeObject(new ArrayList<Student>(studentList));
                        out.flush();
                        break;

                    case "EXIT":
                        out.writeObject("Connection closed.");
                        out.flush();
                        running = false;
                        break;

                    default:
                        out.writeObject("Unknown request.");
                        out.flush();
                        break;
                }
            }
            catch (IOException ex)
            {
                System.out.println("Communication error: " + ex.getMessage());
                running = false;
            }
            catch (ClassNotFoundException ex)
            {
                System.out.println("Unknown object received.");
                running = false;
            }
        }
    }

    private void closeConnection()
    {
        try
        {
            if (in != null)
                in.close();

            if (out != null)
                out.close();

            if (client != null)
                client.close();

            if (listener != null)
                listener.close();

            System.out.println("Server closed.");
        }
        catch (IOException ex)
        {
            System.out.println("Error closing connection: " + ex.getMessage());
        }
    }

    public static void main(String[] args)
    {
        new StudentRecordServer();
    }
}

