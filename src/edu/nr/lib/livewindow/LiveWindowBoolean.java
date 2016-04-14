package edu.nr.lib.livewindow;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class LiveWindowBoolean implements LiveWindowSendable {

	boolean val;
	String name;
	
	private ITable m_table;
	
	public LiveWindowBoolean(String name, boolean val) {
		this.name = name;
		this.val = val;
	}
	
	@Override
	public void initTable(ITable subtable) {
	    m_table = subtable;
	    updateTable();
		
	}

	@Override
	public ITable getTable() {
	    return m_table;
	}

	@Override
	public String getSmartDashboardType() {
		return "LiveWindowBoolean";
	}

	@Override
	public void updateTable() {
		if (m_table != null) {
			m_table.putBoolean(name, val);
		}
	}

	@Override
	public void startLiveWindowMode() {}

	@Override
	public void stopLiveWindowMode() {}

	public void set(boolean val) {
		this.val = val;	
	}

}
