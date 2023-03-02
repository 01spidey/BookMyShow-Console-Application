package com.company;

import java.awt.*;
import java.net.URI;
import java.util.*;
import java.util.List;

class Movie{
    String movieName;
    String movieURL;
    List<Theater> theaterList = new ArrayList<Theater>();
    Scanner scn = new Scanner(System.in);

    Movie(String name, String movieURL){
        movieName = name;
        this.movieURL = movieURL;
    }

    void viewTheaters(){
        for(int i=0;i<theaterList.size();i++){
            System.out.println((i+1)+". "+theaterList.get(i).name);
        }
        System.out.println((theaterList.size()+1)+". Go Back\n\nChoose a Theater : ");
        int op = scn.nextInt();
        System.out.println("\nYou Selected "+theaterList.get(op-1).name+" !!");
    }

    void displayDetails(){
        System.out.println("\nMovie : "+movieName);
        System.out.println("Synopsis : ");
    }

}

class Theater{
    String name;
    String location;
    List<Screen> screen_lst;
    Scanner scn = new Scanner(System.in);

    Theater(String name, String location){
        this.name = name;
        this.location = location;
    }

    void addScreens(List<Screen> screens){
        screen_lst = screens;
        for(Screen screen:screen_lst) screen.movie.theaterList.add(this);
    }

    boolean showTimings(Movie movie, User user){
        List<String> show_timings = new ArrayList<String>();
        Screen cur_screen  = null;
        for (Screen screen : screen_lst) {
            if (screen.movie.movieName.equals(movie.movieName)) {
                cur_screen = screen;
                show_timings = new ArrayList<String>(cur_screen.show_map.keySet());
                break;
            }
        }

        boolean flag = true;
        while(flag) {
            System.out.println("\n---------- Available Timings ----------");
            for (int i = 0; i < show_timings.size(); i++) System.out.println((i + 1) + ". " + show_timings.get(i));
            System.out.print((show_timings.size() + 1) + ". Go Back\n\nChoose an Option : ");
            int op = scn.nextInt();
            if(op<=show_timings.size()) {
                assert cur_screen != null;
                Show cur_show = cur_screen.show_map.get(show_timings.get(op-1));
                System.out.println("\nTiming : "+cur_show.showTime+"\nScreen : "+cur_screen.screen_name);
                cur_show.showSeatGrid();

                System.out.print("\n1. Book Seats\n2. Go Back\n\nChoose an Option : ");

                int ch = scn.nextInt();

                if(ch==1){
                    System.out.println("\n---------- Book Seats ----------");
                    System.out.print("\nEnter No of Tickets : ");
                    int n = scn.nextInt();
                    if(cur_show.canAccommodate(n)){
                        boolean booked = user.bookTicket(this,movie,cur_screen,cur_show,n);
                        if(booked) return false;
                    }else System.out.println("Tata Vro !!");
                }
            }else flag = false;
        }
        return true;
    }

}

class Screen{
    String screen_name;
    Movie movie;
    HashMap<String, Show> show_map = new HashMap<String, Show>();
    int row,col;


    Screen(String screen_name, Movie movie){
        this.screen_name = screen_name;
        this.movie = movie;
    }

    void setGrid(int row,int col){
        this.row = row;
        this.col = col;
    }

    void addShows(String timings){
        String[] timings_arr = timings.split("-");
        for (String s : timings_arr) show_map.put(s, new Show(s,movie,this));
    }

}

class Show{
    Movie movie;
    String showTime;
    String[] seat_grid;
    Screen screen;

    Show(String showTime,Movie movie, Screen screen){
        this.movie = movie;
        this.showTime = showTime;
        this.screen = screen;
        initSeatGrid();
    }

    void initSeatGrid(){
        int n = screen.row*screen.col;
        seat_grid = new String[n];

        for(int i=0;i<n;i++) seat_grid[i] = Integer.toString(i+1);
    }

    void showSeatGrid(){
        System.out.println("\n---------- Seat Arrangement ----------\n");
        int rows = screen.row, cols = screen.col;

        for (int j = 0; j <cols; j++) System.out.print("----");

        System.out.println();

        int a = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%-4s", seat_grid[a]);
                a++;
            }
            System.out.println();
            if (i < rows - 1) {
                for (int j = 0; j <cols; j++) System.out.print("----");
                System.out.println();
            }
        }

        for (int j = 0; j <cols; j++) System.out.print("----");
        System.out.println();
    }

    boolean canAccommodate(int n){
        int seats = 0;
        int size = screen.col*screen.row;
        for(int i=0;i<size;i++){
            if(!seat_grid[i].equals("X")) seats++;
            if(seats>=n) return true;
        }
        return false;
    }

}

class User{
    String name, pass, contact;
//    HashMap<String, Ticket> ticket_map = new HashMap<String, Ticket>();
    ArrayList<Ticket> myTickets = new ArrayList<Ticket>();
    Scanner scn = new Scanner(System.in);

    User(String name, String pass, String contact){
        this.name = name;
        this.pass = pass;
        this.contact = contact;
    }

    boolean viewTickets(){
        int a=1;
        System.out.println("---------- My Tickets ----------");
        if(myTickets.size()!=0) {
            for (Ticket ticket : myTickets) System.out.println((a++) + ". " + ticket.id);
            System.out.print(a + ". Go Back\n\nChoose an Option : ");
            int n = scn.nextInt();
            if (n <= myTickets.size()) {
                Ticket ticket = myTickets.get(n - 1);
                ticket.displayDetails();
                System.out.println("\n1. Cancel Ticket\n2. Go Back\n\nChoose an Option : ");
                int m = scn.nextInt();
                if (m == 1) {
                    cancelTicket(ticket);
                }
                return true;
            }
        }
        else System.out.println("No Tickets !!");

        return false;

    }

    boolean bookTicket(Theater theater, Movie movie, Screen screen, Show show, int n){
        System.out.print("Enter the seat Numbers [1 2 3...] : ");
        String seats_str = scn.nextLine();
        HashSet<Integer> set = new HashSet<Integer>();
        for(String str:seats_str.split(" ")) set.add(Integer.parseInt(str));

        if(set.size()!=n) System.out.println("Invalid Seat Selection !!");
        else{
            boolean flag = true;
            for(Integer i:set){
                if(show.seat_grid[i-1].equals("X")) {
                    flag = false;
                    break;
                }
            }

            if(flag){
                Ticket ticket = new Ticket(theater,screen,show,this,set);
                for(Integer i:set) show.seat_grid[i-1] = "X";
                myTickets.add(ticket);
                System.out.println("\nTicket Booked Successfully !!");
                ticket.displayDetails();
                return true;
            }else System.out.println("Requested Seats already Booked !!");
        }
        return false;
    }

    void cancelTicket(Ticket ticket){
        myTickets.remove(ticket);
        Show show = ticket.show;
        HashSet<Integer> seats = ticket.seats_booked;
        for(Integer i:seats) show.seat_grid[i-1] = Integer.toString(i);
        System.out.println("\nTicket Cancelled Successfully !!");
    }

}

class Ticket{
    String id;
    Theater theater;
    Screen screen;
    Show show;
    User user;
    HashSet<Integer> seats_booked;

    Ticket(Theater theater, Screen screen, Show show, User user, HashSet<Integer> seats) {
        this.theater = theater;
        this.screen = screen;
        this.show = show;
        this.user = user;
        seats_booked = seats;
        id = theater.name + "-" + show.movie.movieName;
    }

    void displayDetails(){
        System.out.println("\n---------- Ticket Details ----------");
        System.out.print("Name : "+user.name+
                "\nMovie : "+screen.movie.movieName+
                "\nTheater : "+theater.name+
                "\nScreen : "+screen.screen_name+
                "\nShow Timing : "+show.showTime+
                "\nBooked Seats : ");

        for(Integer seat:seats_booked) System.out.print(seat+" ");
        System.out.println();
    }

}



public class BookMyShow {

    static HashMap<String,Theater> theater_map = new LinkedHashMap<String, Theater>();
    static HashMap<String,Movie> movie_map = new LinkedHashMap<String, Movie>();
    static HashMap<String, User> user_map = new LinkedHashMap<String, User>();

    static Scanner scn = new Scanner(System.in);

    static {
        Screen screen;
        ArrayList<Screen> screen_lst = new ArrayList<Screen>();

        Theater theater_1 = new Theater("Sri Shakti Cinemas","Tiruppur");

        String show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";

        screen = new Screen("Screen-1", movie_map.containsKey("Ponniyin Selvan-1")?movie_map.get("Ponniyin Selvan-1"):new Movie("Ponniyin Selvan-1","https://www.imdb.com/title/tt10701074/?ref_=ttpl_ov_i"));
        screen.setGrid(5,5);
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-2", movie_map.containsKey("Sita Ramam")?movie_map.get("Sita Ramam"):new Movie("Sita Ramam","https://www.imdb.com/title/tt20850406/?ref_=nv_sr_srsg_0"));
        screen.setGrid(6,6);
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-3", movie_map.containsKey("Anbe Sivam")?movie_map.get("Anbe Sivam"):new Movie("Anbe Sivam","https://www.imdb.com/title/tt0367495/?ref_=nv_sr_srsg_0"));
        screen.setGrid(5,5);
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-4", movie_map.containsKey("Aandavan Kattalai")?movie_map.get("Aandavan Kattalai"):new Movie("Aandavan Kattalai","https://www.imdb.com/title/tt6076366/?ref_=nv_sr_srsg_0"));
        screen.setGrid(7,7);
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        theater_1.addScreens(screen_lst);

        theater_map.put(theater_1.name, theater_1);


//        ---------------------------------------------------------------------------------------------------------------------------------------------
        ArrayList<Screen> screen_lst_1 = new ArrayList<Screen>();

        Theater theater_2 = new Theater("Srinivasa Cinemas","Tiruppur");

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-1", movie_map.containsKey("Madarasapattinam")?movie_map.get("Madarasapattinam"):new Movie("Madarasapattinam","https://www.imdb.com/title/tt1365030/?ref_=nv_sr_srsg_0"));
        screen.setGrid(5,5);
        screen.addShows(show_timings);
        screen_lst_1.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-2", movie_map.containsKey("Jai Bhim")?movie_map.get("Jai Bhim"):new Movie("Jai Bhim","https://www.imdb.com/title/tt15097216/?ref_=nv_sr_srsg_0"));
        screen.setGrid(7,7);
        screen.addShows(show_timings);
        screen_lst_1.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-3", movie_map.containsKey("Enthiran")?movie_map.get("Enthiran"):new Movie("Enthiran","https://www.imdb.com/title/tt1305797/?ref_=nv_sr_srsg_2"));
        screen.setGrid(5,5);
        screen.addShows(show_timings);
        screen_lst_1.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-4", movie_map.containsKey("Ponniyin Selvan-1")?movie_map.get("Ponniyin Selvan-1"):new Movie("Ponniyin Selvan-1","https://www.imdb.com/title/tt10701074/?ref_=ttpl_ov_i"));
        screen.setGrid(6,6);
        screen.addShows(show_timings);
        screen_lst_1.add(screen);
        movie_map.put((screen.movie).movieName,screen.movie);

        theater_2.addScreens(screen_lst_1);

        theater_map.put(theater_2.name, theater_2);

    }

    static String userSignUp(String name, String pass, String contact){
        User user = new User(name,pass,contact);
        user_map.put(name+"-"+pass,user);
        return name+"-"+pass;
    }

    static String askFirst(){
        System.out.println("\n---------- Book My Shows !! ----------");
        System.out.print("1. Sign Up\n2. Sign In\n3. Exit\n\nChoose an Option : ");
        int op = scn.nextInt();

        if(op==1){
            System.out.println("\n---------- User Sign Up ----------");
            System.out.print("Name : ");
            scn.nextLine();
            String name = scn.nextLine();
            System.out.print("Password : ");
            String pass = scn.nextLine();
            if(user_map.containsKey(name+"-"+pass)){
                System.out.println("\nUsername Not Available !!");
                return "null";
            }
            System.out.print("Contact : +91 ");
            String contact = scn.nextLine();
            return userSignUp(name,pass,contact);
        }
        else if(op==2){
            System.out.println("\n---------- User Sign In ----------");
            System.out.print("Name : ");
            scn.nextLine();
            String name = scn.nextLine();
            System.out.print("Password : ");
            String pass = scn.nextLine();
            if(user_map.containsKey(name+"-"+pass)) return name+"-"+pass;
            else{
                System.out.println("\nUser Not Found !!");
                return "null";
            }
        }
        else return "exit";

    }

    static void visitPage(String url){
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI uri = new java.net.URI(url);
            desktop.browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void viewMovies(User user){
        boolean out_flag = true;
        while(out_flag){
            System.out.println("\nWondering What to Watch?! ...");
            List<String> movies = new ArrayList<String>(movie_map.keySet());
            for (int i = 0; i < movies.size(); i++) System.out.println((i + 1) + ". " + movies.get(i));
            System.out.print((movies.size() + 1) + ". Go Back\n\nChoose Something to watch : ");
            int op = scn.nextInt();

            if (op <= movies.size()) {
                boolean flag = true;
                while (flag) {
                    Movie movie = movie_map.get(movies.get(op - 1));
                    System.out.println("\nMovie : " + movie.movieName);
                    System.out.print("1. About Movie\n2. Book Ticket\n3. Go Back\n\nChoose an Action : ");
                    int b = scn.nextInt();

                    if (b == 1) {
                        System.out.println("Redirecting to IMDB...");
                        visitPage(movie.movieURL);
                    }
                    else if (b == 2) {
                        boolean flag_2 = true;
                        while (flag_2) {
                            System.out.println("\n---------- Running On ----------");
                            List<Theater> theaters = movie.theaterList;
                            for (int i = 0; i < theaters.size(); i++)
                                System.out.println((i + 1) + ". " + theaters.get(i).name);
                            System.out.print((theaters.size() + 1) + ". " + "Go Back\n\nChoose a Theater : ");
                            int ch = scn.nextInt();
                            if (ch <= theaters.size()) {
                                Theater theater = theaters.get(ch-1);
                                System.out.print("\nTheater : "+theater.name+"\nLocation : "+theater.location);
                                flag_2 = theater.showTimings(movie,user);
                            } else flag_2 = false;
                        }
                    } else flag = false;
                }
            } else out_flag = false;
        }
    }

    public static void main(String[] args) {

        String cur_login = "";

        while(true){
            cur_login = askFirst();

            if(cur_login.equals("exit")){
                System.out.println("\nShutting the System Down !!");
                break;
            }
            else if(!cur_login.equals("null")){
                User user = user_map.get(cur_login);
                System.out.println("\nWelcome "+user.name+" !!");
                while(true) {
                    System.out.print("\n---------- User Menu ----------\n1. Book Ticket\n2. My Tickets\n3. Logout\n\nChoose an option : ");
                    int op = scn.nextInt();
                    if (op == 1) {
                        viewMovies(user);
                    } else if (op == 2) {
                        boolean flag = true;
                        while(flag) {
                            int a = 1;
                            flag = user.viewTickets();
                        }

                    } else break;
                }

            }
        }


    }
}
