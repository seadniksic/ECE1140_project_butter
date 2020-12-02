//package sample;
//
//import javafx.concurrent.Task;
//
//public class GetUpdatedOccupancyTask extends Task<Track_Model_Murphy_GUI> {
//
//    @Override
//    protected Track_Model_Murphy_GUI call() throws Exception {
//        //TODO: Insert methods
//
//        if(simulate_Was_Clicked){
//            for(int i = 0; i < 10; i++){
//                update_Occupancy(param_Line_Index, 0, 10.0);
//                swap_To_Line_Scene(param_Line_Index, param_GridPane);
//            }
//
//        } else{
//            // Set up before getting distances from trains
//            System.out.println("Spawning Train...");
////                        spawn_Train_In_Yard(param_Line_Index, 62);
//            spawn_Train_In_Yard(param_Line_Index, 1);
//            swap_To_Line_Scene(param_Line_Index, param_GridPane);
////                        set_Switch_At_Block(param_Line_Index,76, false);
////                        set_Switch_At_Block(param_Line_Index,77, true);
////                        set_Switch_At_Block(param_Line_Index,101, true);
//            set_Switch_At_Block(param_Line_Index, 3, true);
//            set_Switch_At_Block(param_Line_Index, 21, true);
//            simulate_Was_Clicked = true;
//        }
//
//
//
//        return null;
//    }
//}
