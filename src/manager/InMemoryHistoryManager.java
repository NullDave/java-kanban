package manager;

import task.EpicTask;
import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList tasks;  // сделал вложенным классов

    public InMemoryHistoryManager() {
        tasks = new CustomLinkedList();
    }

    @Override
    public void addTask(Task task) {
        tasks.addLast(task);

    }

    @Override
    public void removeTask(int id) {
     tasks.removeTask(id);
    }

    @Override
    public List<Task> getHistory() {
        return tasks.getAll();
    }


    private class CustomLinkedList {
        private Node<Task> last;
        private Node<Task> first;

        private final Map<Integer,Node<Task>> hashMap;

        private CustomLinkedList() {
            hashMap = new HashMap<>();
        }

        public void addLast(Task task) {
            final Node<Task> currentNode = hashMap.get(task.getId());
            if(currentNode != null) {
                removeNode(currentNode);
            }
            final Node<Task> oldLast = last;
            final Node<Task> newNode = new Node<>(oldLast, task, null);
            last = newNode;
            if (oldLast == null) {
                first = newNode;
            }else {
                oldLast.setNext(newNode);
            }
            hashMap.put(task.getId(),newNode);
        }

        public ArrayList<Task> getAll(){
            ArrayList<Task> tasks = new ArrayList<>();
            Node<Task> node = first;
            if(first == null)
                return tasks;
            while (node != null){
               tasks.add(node.getData());
               node = node.getNext();
           }
           return tasks;
        }

        public void removeTask(int id){
            final Node<Task> node = hashMap.get(id);
            if (node == null)
               return;
            if(node.getData() instanceof EpicTask){  // если удаляем эпик то сначала удаляем подзадачи эпика, если есть.
                EpicTask epicTask = (EpicTask) node.getData();
                for (int subTaskId: epicTask.getListSubTaskId()) {
                    final Node<Task> subNode = hashMap.get(subTaskId);
                    if (subNode != null){
                        hashMap.remove(subTaskId);
                        removeNode(subNode);
                    }
                }
            }
            hashMap.remove(id);
            removeNode(node);
        }

        private void removeNode(Node<Task> node){
            final Node<Task> prev = node.getPrev();
            final Node<Task> next = node.getNext();
            if(prev == null) {
                first = next;
            }
            else {
                prev.setNext(next);
            }
            if(next == null) {
                last = prev;
            }
            else {
                next.setPrev(prev);
            }
        }



    }

}
