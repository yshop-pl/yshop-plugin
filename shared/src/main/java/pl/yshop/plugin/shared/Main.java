package pl.yshop.plugin.shared;

public class Main {
    public static void main(String[] args) {
        ApiRequests apiRequests = new ApiRequests("eeee", "3", "4");
        apiRequests.getCommandsToExecute();
    }
}