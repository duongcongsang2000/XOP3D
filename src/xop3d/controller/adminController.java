package xop3d.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import xop3d.bean.Uploadfile;
import xop3d.entity.admin;
import xop3d.entity.groupProduct;
import xop3d.entity.order;
import xop3d.entity.orderDetail;
import xop3d.entity.product;
import xop3d.entity.productDetail;
import xop3d.entity.user;

@Transactional
@Controller
@RequestMapping("admin")
public class adminController {
	@Autowired
	SessionFactory factory;
	@Autowired
	ServletContext context;
	
	@RequestMapping("taikhoan")
	public String taikhoan() {
		return "admin/taikhoan";
	}
	
	@RequestMapping("login_admin")
	public String login()
	{
		return "admin/login_admin" ;
	}
	public static String username = "Guest";
	public static admin userlogin;
	@RequestMapping(value = "login_admin" ,method=RequestMethod.POST)
	public String dangnhap(Model model,HttpServletRequest re) throws ParseException {
		Session session = this.factory.getCurrentSession();
		String hql = "FROM admin";
		Query query =session.createQuery(hql);
		List<admin> list = query.list();
		for (admin admin : list) {
			if(admin.getAdminName().trim().equals(re.getParameter("user").trim()) && admin.getPassword().trim().equals(re.getParameter("pass").trim())) {
				username = admin.getAdminName();
				model.addAttribute("username", username);
				userlogin = admin;
				return "admin/index";
			}
		}
		model.addAttribute("tb1", "Đăng nhập thất bại vui lòng kiếm tra lại !");
		return "admin/login_admin";
	}
	
	@RequestMapping(value = "grtaikhoan", method = RequestMethod.POST)
	public String Themtk(Model model,HttpServletRequest re) {
		user us = new user();
		Session s = this.factory.openSession();
		Transaction t = s.beginTransaction();
		this.tb="";
			try{
				if(s.get(user.class, re.getParameter("username").trim()) == null)
				{
					us.setUsername(re.getParameter("username"));
					us.setPassword(re.getParameter("password"));
					us.setFullname(re.getParameter("fullname"));
					String day=re.getParameter("birthday");
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date day1=formatter.parse(day);
					us.setBirthday(day1);
					us.setGender(Integer.parseInt(re.getParameter("gender")));
					us.setEmail(re.getParameter("email"));
					us.setPhone(Integer.parseInt(re.getParameter("phone")));
					us.setAddress(re.getParameter("address"));
					
					s.save(us);
					t.commit();
					this.tb= "Thêm  thành công !";
					model.addAttribute("tb",this.tb);
				} else {
					model.addAttribute("tb", "Tài Khoản đã tồn tại , vui lòng thử lại");
				}
			
	} catch (Exception e) {
		t.rollback();
		this.tb="Thêm thất bại,vui lòng kiểm tra lại";
		model.addAttribute("tb", this.tb);
	} finally {
		s.close();
	}
	return "redirect:/admin/taikhoan.htm";
	}
	@ModelAttribute("listtk")
	public List<user> getlisttk() {
		Session s = this.factory.getCurrentSession();
		String hql = "from user ";
		Query query = s.createQuery(hql);
		List<user> list = query.list();
		return list;
	}
	@RequestMapping(value = "grtaikhoan", method = RequestMethod.GET)
	public String loadthemtk(Model model, HttpServletRequest re) {
		return "admin/index";
	}
	@RequestMapping(value = "edittk", method = RequestMethod.GET)
	public String suatk(Model model, @RequestParam("taikhoan") String taikhoan, HttpServletRequest re)
			throws IllegalStateException, IOException {
		Session s = this.factory.getCurrentSession();
		String hql = "from user";
		Query query = s.createQuery(hql);
		List<user> list = query.list();
		for (user a : list) {
			{
				user tk = (user) s.load(user.class,taikhoan);
				model.addAttribute("tk",tk);
			}
		}
		
		return "admin/edittk";
	}
	@RequestMapping(value = "edittk", method = RequestMethod.POST)
	public String edittk(Model model, HttpServletRequest re)
			throws IllegalStateException, IOException, ParseException {
		Session s = this.factory.openSession();
		String hql = "from user";
		Query query = s.createQuery(hql);
		Transaction t = s.beginTransaction();
		List<user> list = query.list();
		user tk = new user();
		this.tb="";
		String taikhoan = re.getParameter("taikhoan");
		for (user a : list) {
			{
				tk = (user) s.load(user.class, taikhoan);
				model.addAttribute("tk", tk);
			}
		}
		tk.setUsername(re.getParameter("username"));
		tk.setPassword(re.getParameter("password"));
		tk.setFullname(re.getParameter("fullname"));
		tk.setGender(Integer.parseInt(re.getParameter("gender")));
		String day=re.getParameter("birthday");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date day1=formatter.parse(day);
		tk.setBirthday(day1);
		tk.setEmail(re.getParameter("email"));
		tk.setPhone(Integer.parseInt(re.getParameter("phone")));
		tk.setAddress(re.getParameter("address"));
//		
		try {
			s.update(tk);
			t.commit();
			this.tb="Cập nhật SP thành công !";
			model.addAttribute("tb",this.tb );
		} catch (Exception e) {
			t.rollback();
			this.tb="Cập nhật thất bại,vui lòng kiểm tra lại";
			model.addAttribute("tb",this.tb );
		} finally {
			s.close();

		}
		return "redirect:/admin/taikhoan.htm";
	}
	@RequestMapping("deletetk")
	public String deletetk(Model model, @RequestParam("taikhoan") String taikhoan) {
		Session s = this.factory.openSession();
		Transaction t = s.beginTransaction();
		String hql = "from user ";
		Query query = s.createQuery(hql);
		List<user> list = query.list();
		user tk  = new user();
		this.tb="";
		for (user a : list) {
			{
				tk = (user) s.load(user.class, taikhoan);
			}
		}
		try
		{
			s.delete(tk);
			t.commit();
			this.tb="Xóa thành công";
			model.addAttribute("tb", this.tb);
		
		}catch (Exception e) {
			t.rollback();
			this.tb="Xoá thất bại,vui lòng kiểm tra lại";
			model.addAttribute("tb", this.tb);
		}
		finally {
			s.close();
		}
		return "redirect:/admin/taikhoan.htm";
	}
	@RequestMapping("product")
	public String pr(Model model, @ModelAttribute("prod") product pr) {

		return "admin/index";
	}
	@RequestMapping("donhang")
	public String dh(Model model) {

		return "admin/donhang";
	}
	String tb="";
	@RequestMapping(value = "gr-product", method = RequestMethod.POST)
	public String them(Model model, HttpServletRequest re) {
		groupProduct gpr = new groupProduct();
		Session s = this.factory.openSession();
		Transaction t = s.beginTransaction();
		this.tb="";
		try {
			if (s.get(groupProduct.class, re.getParameter("id").trim()) == null) {
				gpr.setId(re.getParameter("id"));
				gpr.setName(re.getParameter("name"));
				gpr.setContent(re.getParameter("content"));
				gpr.setStatus(1);
				s.save(gpr);
				t.commit();
				this.tb= "Thêm nhóm thành công !";
				model.addAttribute("tb",this.tb);
			} else {
				model.addAttribute("tb", "Nhóm sản phẩm đã tồn tại , vui lòng thử lại");
			}
		} catch (Exception e) {
			t.rollback();
			this.tb="Thêm thất bại,vui lòng kiểm tra lại";
			model.addAttribute("tb", this.tb);
		} finally {
			s.close();
		}
		return "redirect:/admin/product.htm";
	}
	@RequestMapping(value = "gr-product", method = RequestMethod.GET)
	public String loadthem(Model model, HttpServletRequest re) {
		return "admin/index";
	}
	@RequestMapping(value = "productde", method = RequestMethod.GET)
	public String loadthem1(Model model, HttpServletRequest re) {
		return "admin/index";
	}

	@RequestMapping(value = "product", method = RequestMethod.GET)
	public String loadthem(Model model, HttpServletRequest re, @ModelAttribute("prod") product pr) {
		model.addAttribute("tb",this.tb);
		return "admin/index";
	}

	@RequestMapping(value = "editpr", method = RequestMethod.GET)
	public String loadthem(Model model, @RequestParam("idSanPham") int idSanPham, HttpServletRequest re,
			@RequestParam("img1") String img1, @RequestParam("img2") String img2, @RequestParam("img3") String img3)
			throws IllegalStateException, IOException {
		Session s = this.factory.getCurrentSession();
		String hql = "from productDetail ";
		Query query = s.createQuery(hql);
		List<productDetail> list = query.list();

		for (productDetail a : list) {
			if (a.getId() == idSanPham) {
				productDetail prd = (productDetail) s.load(productDetail.class, idSanPham);
				model.addAttribute("prd", prd);
			}
		}
		return "admin/edit";

		
	}
	@Autowired
	Uploadfile uploadFile1;
	@RequestMapping(value = "product", method = RequestMethod.POST)
	public String themsp(Model model, HttpServletRequest re, @RequestParam("img1") MultipartFile img1,
			@RequestParam("img2") MultipartFile img2, @RequestParam("img3") MultipartFile img3)
			throws ServletException, IOException {
		product pr = new product();
		Session s = this.factory.openSession();
		Transaction t = s.beginTransaction();
		this.tb="";
		if (img1.isEmpty() || img2.isEmpty() || img3.isEmpty()) {
			model.addAttribute("tb", "Vui lòng chọn đầy đủ hình ảnh");
		} else {
			Integer sale;
			int price = Integer.parseInt(re.getParameter("p"));
			sale = Integer.parseInt(re.getParameter("s"));
			
			try {
				pr.setGroupProduct((groupProduct) s.get(groupProduct.class, re.getParameter("grid")));
				pr.setDate(new Date());
				pr.setSold(0);
				pr.setId(re.getParameter("id"));
				pr.setName(re.getParameter("name"));
				pr.setColer(re.getParameter("coler"));
				pr.setContent(re.getParameter("content"));
				pr.setSale(sale);
				pr.setPrice(price);
				pr.setStatus(1);
				
				String BaseDir=this.uploadFile1.basePath;
				//String img1Path = this.context.getRealPath("/resources/img/pro/" + img1.getOriginalFilename());
				String img1Path= BaseDir + File.separator + img1.getOriginalFilename();
				img1.transferTo(new File(img1Path));
				String img2Path= BaseDir + File.separator + img2.getOriginalFilename();
				//String img2Path = this.context.getRealPath("/resources/img/pro/" + img2.getOriginalFilename());
				img2.transferTo(new File(img2Path));
				String img3Path= BaseDir + File.separator + img3.getOriginalFilename();
				//String img3Path = this.context.getRealPath("/resources/img/pro/" + img3.getOriginalFilename());
				img3.transferTo(new File(img3Path));
				model.addAttribute("img1", img1.getOriginalFilename());
				model.addAttribute("img2", img1.getOriginalFilename());
				model.addAttribute("img3", img1.getOriginalFilename());
				pr.setImg1(img1.getOriginalFilename());
				pr.setImg2(img2.getOriginalFilename());
				pr.setImg3(img3.getOriginalFilename());
				s.save(pr);
				t.commit();
				this.tb="Thêm SP thành công";
				model.addAttribute("tb", this.tb);
				getpr(model);
			} catch (Exception e) {
				t.rollback();
				this.tb="Thêm thất bại !";
				model.addAttribute("tb", this.tb);
			} finally {
				loadlistgr(model);
				s.close();
			}
		}
		return "admin/index";
	}

	@RequestMapping(value = "productde", method = RequestMethod.POST)
	public String them2(Model model, HttpServletRequest re) {
		productDetail prd = new productDetail();
		Session s = this.factory.openSession();
		Transaction t = s.beginTransaction();
		this.tb="";
		try {

			prd.setQuanlity(Integer.parseInt(re.getParameter("quanlity")));
			prd.setSize(Integer.parseInt(re.getParameter("size")));
			prd.setProduct((product) s.get(product.class, re.getParameter("prid")));
			s.save(prd);
			t.commit();
			this.tb="Thêm chi tiết thành công !";
			model.addAttribute("tb", this.tb);
			loadlistgr(model);

		} catch (Exception e) {
			t.rollback();
			this.tb="Thêm thất bại !";
			model.addAttribute("tb", this.tb);
		} finally {
			s.close();
		}
		return "redirect:/admin/product.htm";
	}

	@ModelAttribute("grpr")
	public List<groupProduct> getgr() {
		Session s = this.factory.getCurrentSession();
		String hql = "from groupProduct";
		Query query = s.createQuery(hql);
		List<groupProduct> list = query.list();
		return list;
	}

	@ModelAttribute("prr")
	public List<product> getgrr() {
		Session s = this.factory.getCurrentSession();
		String hql = "from product";
		Query query = s.createQuery(hql);
		List<product> list = query.list();
		return list;
	}
	public void getpr(Model model) {
		Session s = this.factory.getCurrentSession();
		String hql = "from product";
		Query query = s.createQuery(hql);
		List<product> list = query.list();
		model.addAttribute("prr",list);
	}
	@ModelAttribute("listpr")
	public List<productDetail> getlistgr() {
		Session s = this.factory.getCurrentSession();
		String hql = "from productDetail ";
		Query query = s.createQuery(hql);
		List<productDetail> list = query.list();
		return list;
	}
	@ModelAttribute("listdh")
	public List<orderDetail> getlistdh() {
		Session s = this.factory.getCurrentSession();
		String hql = "from orderDetail ";
		Query query = s.createQuery(hql);
		List<orderDetail> list = query.list();
		return list;
	}
	public void getdh(Model model) {
		Session s = this.factory.getCurrentSession();
		String hql = "from orderDetail ";
		Query query = s.createQuery(hql);
		List<orderDetail> list = query.list();
		Session s1 = this.factory.getCurrentSession();
		String hql1 = "from order";
		Query query1 = s1.createQuery(hql1);
		List<order> list1 = query1.list();
		model.addAttribute("listdh1",list1);
		model.addAttribute("listdh",list);
		
	}
	public List<productDetail> loadlistgr(Model modela) {

		Session s = this.factory.getCurrentSession();
		String hql = "from productDetail ";
		Query query = s.createQuery(hql);
		List<productDetail> list = query.list();
		modela.addAttribute("listpr", list);
		return list;
	}
	@RequestMapping("deleteod")
	public String deleteod(Model model, @RequestParam("id") int id) {
		Session s = this.factory.openSession();
		Transaction t = s.beginTransaction();
		String hql = "from order ";
		Query query = s.createQuery(hql);
		List<order> list = query.list();
		order od = new order();
		for (order a : list) {
			if (a.getId() == id) {
				od = (order) s.load(order.class, id);
				
			}
		}
		try {
			s.delete(od);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		}
		finally {
			s.close();
			getdh(model);
		}
		return "admin/donhang";
	}
	@RequestMapping("xacnhan")
	public String xacnhanod(Model model, @RequestParam("id") int id) {
		Session s = this.factory.openSession();
		Transaction t = s.beginTransaction();
		String hql = "from order ";
		Query query = s.createQuery(hql);
		List<order> list = query.list();
		order od = new order();
		for (order a : list) {
			if (a.getId() == id) {
				od = (order) s.load(order.class, id);
				od.setStatus(0);
			}
		}
		try {
			s.update(od);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		}
		finally {
			s.close();
			getdh(model);
		}
		return "admin/donhang";
	}
	@RequestMapping("deletepr")
	public String deletepr(Model model, @RequestParam("idSanPham") int idSanPham) {
		Session s = this.factory.openSession();
		Transaction t = s.beginTransaction();
		String hql = "from productDetail ";
		Query query = s.createQuery(hql);
		List<productDetail> list = query.list();
		productDetail prd  = new productDetail();
		this.tb="";
		for (productDetail a : list) {
			if (a.getId() == idSanPham) {
				prd = (productDetail) s.load(productDetail.class, idSanPham);
				
			}
		}
		try {
			s.delete(prd);
			this.tb="Xóa thành công";
			model.addAttribute("tb", this.tb);
			t.commit();
		} catch (Exception e) {
			t.rollback();
			this.tb="Xóa thất bại";
			model.addAttribute("tb", this.tb);
		}
		finally {
			s.close();
		}
		return "redirect:/admin/product.htm";
	}
	@RequestMapping("deletepr-all")
	public String deleteall(Model model, @RequestParam("idSanPham") int idSanPham) {
		Session s = this.factory.openSession();
		Transaction t = s.beginTransaction();
		String hql = "from productDetail ";
		Query query = s.createQuery(hql);
		List<productDetail> list = query.list();
		productDetail prd  = new productDetail();
		product pr = new product();
		this.tb="";
		for (productDetail a : list) {
			if (a.getId() == idSanPham) {
				prd = (productDetail) s.load(productDetail.class, idSanPham);
				pr = (product) s.load(product.class,prd.getProduct().getId());
				
			}
		}
		try {
			s.delete(prd);
			s.delete(pr);
			this.tb= "Xóa thành công";
			model.addAttribute("tb",this.tb);
			t.commit();
		} catch (Exception e) {
			t.rollback();
			this.tb= "Xóa thất bại";
			model.addAttribute("tb",this.tb);
		}
		finally {
			s.close();
		}
		return "redirect:/admin/product.htm";
	}
	@Autowired
	Uploadfile uploadFile;
	@RequestMapping(value = "editpr", method = RequestMethod.POST)
	public String editpr(Model model, HttpServletRequest re, @RequestParam("img1") MultipartFile img1,
			@RequestParam("img2") MultipartFile img2, @RequestParam("img3") MultipartFile img3)
			throws IllegalStateException, IOException {
		Session s = this.factory.openSession();
		Session s1 = this.factory.openSession();
		String hql = "from productDetail ";
		Query query = s.createQuery(hql);
		Transaction t = s.beginTransaction();
		Transaction t1 = s1.beginTransaction();
		List<productDetail> list = query.list();
		productDetail prd = new productDetail();
		this.tb="";
		product pr = new product();
		groupProduct gpr = new groupProduct();
		int idSanPham = Integer.parseInt(re.getParameter("idSanPham"));
		for (productDetail a : list) {
			if (a.getId() == idSanPham) {
				prd = (productDetail) s.load(productDetail.class, idSanPham);
				model.addAttribute("prd", prd);
			}
		}
		gpr = (groupProduct) s.load(groupProduct.class, re.getParameter("grid"));
		pr.setId(re.getParameter("id"));
		pr.setGroupProduct(gpr);
		pr.setName(re.getParameter("name"));
		pr.setDate(new Date());
		pr.setColer(re.getParameter("coler"));
		pr.setContent(re.getParameter("content"));
		pr.setSale(Integer.parseInt(re.getParameter("s")));
		pr.setPrice(Integer.parseInt(re.getParameter("p")));
		pr.setSold(0);
		try {
			String BaseDir=this.uploadFile.basePath;
			//String img1Path = this.context.getRealPath("/resources/img/pro/" + img1.getOriginalFilename());
			String img1Path= BaseDir + File.separator + img1.getOriginalFilename();
			img1.transferTo(new File(img1Path));
			String img2Path= BaseDir + File.separator + img2.getOriginalFilename();
			//String img2Path = this.context.getRealPath("/resources/img/pro/" + img2.getOriginalFilename());
			img2.transferTo(new File(img2Path));
			String img3Path= BaseDir + File.separator + img3.getOriginalFilename();
			//String img3Path = this.context.getRealPath("/resources/img/pro/" + img3.getOriginalFilename());
			img3.transferTo(new File(img3Path));
			pr.setImg1(img1.getOriginalFilename());
			pr.setImg2(img2.getOriginalFilename());
			pr.setImg3(img3.getOriginalFilename());
		} catch (Exception e) {
			pr.setImg1(re.getParameter("img1"));
			pr.setImg1(re.getParameter("img2"));
			pr.setImg1(re.getParameter("img3"));
		}
		model.addAttribute("img1", img1.getOriginalFilename());
		model.addAttribute("img2", img1.getOriginalFilename());
		model.addAttribute("img3", img1.getOriginalFilename());

		pr.setStatus(Integer.parseInt(re.getParameter("status")));
		prd.setProduct(pr);
		prd.setId(idSanPham);
		prd.setQuanlity(Integer.parseInt(re.getParameter("quanlity")));
		prd.setSize(Integer.parseInt(re.getParameter("size")));
		prd.setStatus(1);
		try {
			s1.update(pr);
			s.update(prd);
			t.commit();
			t1.commit();
			this.tb="Cập nhật SP thành công !";
			model.addAttribute("tb",this.tb );
		} catch (Exception e) {
			t.rollback();
			t1.rollback();
			this.tb="Cập nhật thất bại,vui lòng kiểm tra lại";
			model.addAttribute("tb",this.tb );
		} finally {
			s.close();
			s1.close();
			loadlistgr(model);

		}
		return "redirect:/admin/product.htm";
	}
}
