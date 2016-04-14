package edu.nr.lib.livewindow;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class LiveWindowNumber implements LiveWindowSendable {

	double num;
	String name;
	
	private ITable m_table;
	
	public LiveWindowNumber(String name, double num) {
		this.name = name;
		this.num = num;
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
		return "LiveWindowNumber";
	}

	@Override
	public void updateTable() {
		if (m_table != null) {
			m_table.putNumber(name, num);
		}
	}

	@Override
	public void startLiveWindowMode() {}

	@Override
	public void stopLiveWindowMode() {}

	public void set(double num) {
		this.num = num;		
	}

}
