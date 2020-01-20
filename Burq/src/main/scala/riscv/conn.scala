package riscv

import chisel3._
import chisel3.util.Cat
import chisel3.util.Fill
import chisel3.core.SInt
class conn extends Module{
	val io = IO(new Bundle{


		val output = Output(SInt(32.W))


})
	//modules
	//starting 5 stage registers
	val if_id_c = Module(new if_id_reg)
	val id_ex_c = Module(new id_ex_reg)
	val ex_mem_c = Module(new ex_mem_reg)
	val mem_wb_c = Module(new mem_wb_reg)

	val controlc = Module(new Control)
	val immediatec = Module(new Immediate)
	val reg32c = Module(new reg32)
	val aluc = Module(new alu)
	val aluconc = Module(new AluControl)
	val instruction_c = Module(new instruction)
	val pc_main = Module(new pc)
	//val jalr_c = Module(new jalr) //n
	val mem_c = Module(new main_mem)


	pc_main.io.input := pc_main.io.pc4
	//immediatec.io.pc := pc_main.io.pc_output

	instruction_c.io.write_address := pc_main.io.pc_output(11,2)
	controlc.io.OpCode :=instruction_c.io.r_data(6,0)

	//register

	reg32c.io.imm_rs1 := instruction_c.io.r_data(19,15)
	reg32c.io.imm_rs2 := instruction_c.io.r_data(24,20)
	//reg32c.io.rd_reg:= instruction_c.io.r_data(11,7)
	//reg32c.io.reg_write:=controlc.io.RegWrite

	//immediatec
	immediatec.io.instruction := if_id_c.io.instruction_output
	immediatec.io.pc := if_id_c.io.pc_output

	//5  stage AluControl
	aluconc.io.aluopp := controlc.io.AluOp
	aluconc.io.Func3:= if_id_c.io.instruction_output(14,12)
	aluconc.io.Func7:= if_id_c.io.instruction_output(30)


// operand A
	when(controlc.io.Operand_aSel==="b00".U){

		id_ex_c.io.cont_opA := (reg32c.io.rs1_output).asUInt

	}.elsewhen(controlc.io.Operand_aSel==="b10".U){

		//luc.io.A := pc_main.io.pc4.asSInt
		id_ex_c.io.cont_opA := (if_id_c.io.pc4_output).asUInt

	}.elsewhen(controlc.io.Operand_aSel==="b01".U){

		//aluc.io.A := pc_main.io.pc_output.asSInt
		id_ex_c.io.cont_opA := (if_id_c.io.pc_output).asUInt

	}.elsewhen(controlc.io.Operand_aSel==="b11".U){

		//aluc.io.A := reg32c.io.rs1_output
		id_ex_c.io.cont_opA := (reg32c.io.rs1_output).asUInt

	}.otherwise{

		//aluc.io.A:=DontCare
			id_ex_c.io.cont_opA := DontCare

	}
//Operand B
	when(controlc.io.ExtendSel ==="b00".U && controlc.io.Operand_bSel==="b1".U){

		id_ex_c.io.cont_opB := immediatec.io.I_Type.asUInt
	}.elsewhen(controlc.io.ExtendSel==="b10".U && controlc.io.Operand_bSel==="b1".U){
		id_ex_c.io.cont_opB:= immediatec.io.U_Type.asUInt

	}.elsewhen(controlc.io.ExtendSel==="b01".U && controlc.io.Operand_bSel==="b1".U){

		id_ex_c.io.cont_opB := immediatec.io.S_Type.asUInt

	}.otherwise{

		id_ex_c.io.cont_opB:= (reg32c.io.rs2_output).asUInt

	}



	//if_id_reg
	if_id_c.io.pc_input := pc_main.io.pc_output
	if_id_c.io.pc4_input := pc_main.io.pc4
	val temp =instruction_c.io.r_data
	if_id_c.io.instruction_input := temp

	//id_ex_reg

	id_ex_c.io.rs2_in := (reg32c.io.rs2_output).asUInt

	id_ex_c.io.rdsel := if_id_c.io.instruction_output(11,7)
	id_ex_c.io.cont_memwrite := controlc.io.MemWrite

	id_ex_c.io.cont_memread := controlc.io.MemRead
	id_ex_c.io.cont_regwrite := controlc.io.RegWrite
	id_ex_c.io.cont_memtoreg := controlc.io.MemToReg
	id_ex_c.io.alucont_in := aluconc.io.AluC

	//id_ex_c.io.cont_op := controlc.io.Operand_aSel

	//ALU
	 aluc.io.alu_c := id_ex_c.io.id_ex_alucont_out
	 aluc.io.A := (id_ex_c.io.cont_opA).asSInt
	 aluc.io.B := (id_ex_c.io.cont_opB).asSInt

	//ex_mem_reg
	ex_mem_c.io.cont_memwrite := id_ex_c.io.cont_memwrite
	ex_mem_c.io.cont_memread := id_ex_c.io.cont_memread
	ex_mem_c.io.cont_regwrite := id_ex_c.io.cont_regwrite
	ex_mem_c.io.id_ex_rs2in := id_ex_c.io.id_ex_rs2out
	ex_mem_c.io.id_ex_rdsel := id_ex_c.io.id_ex_rdsel_out
	ex_mem_c.io. memtoreg_in :=  id_ex_c.io.cont_memtoreg_out
	ex_mem_c.io.alu_out := (aluc.io.alu_output).asUInt

	//main main_mem
	mem_c.io.alu_out := (ex_mem_c.io.exme_alu_out(9,2)).asUInt
	mem_c.io.rs2 := (ex_mem_c.io.exme_rs2_out).asSInt
	mem_c.io.mem_write := ex_mem_c.io.exme_memwrit_out
	mem_c.io.mem_read := ex_mem_c.io.exme_memread_out

	//mem_wb_c
		mem_wb_c.io.memtoreg_in := ex_mem_c.io.memtoreg_out
		mem_wb_c.io.memwb_rdsel_in := ex_mem_c.io.exme_rdsel_out
		mem_wb_c.io.memwb_aluout_in := ex_mem_c.io.exme_alu_out
		mem_wb_c.io.memwb_dataout_in := (mem_c.io.mem_out).asUInt
		mem_wb_c.io.memwb_regwrite_in := ex_mem_c.io.exme_regwrite_out


	//writeback
	reg32c.io.rd_reg := mem_wb_c.io.mem_web_rdsel_out
	reg32c.io.reg_write := mem_wb_c.io.mem_web_regwrite_out

	//writeback


		when (mem_wb_c.io.mem_web_memtoreg_out=== 1.U){
			//reg32c.io.write_data := mem_c.io.mem_out
			reg32c.io.write_data := (mem_wb_c.io.mem_web_dataout_out).asSInt
		}.otherwise{
					when(reg32c.io.reg_write === 1.U){
						reg32c.io.write_data := (mem_wb_c.io.mem_web_aluout_out).asSInt
					}.otherwise{
						reg32c.io. write_data := 0.S
					}

		}
		io.output := reg32c.io.write_data


}
