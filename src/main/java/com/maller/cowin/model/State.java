package com.maller.cowin.model;

public class State {
    int state_id;
    String state_name;

    public State() {}
    
    public State(int id, String name) {
        this.state_id = id;
        this.state_name = name;
    }

    public String getState_name(){
        return this.state_name;
    }

    public int getState_id() {
        return this.state_id;
    }

	@Override
	public String toString() {
		return "State [state_id=" + state_id + ", state_name=" + state_name + "]";
	}
}

