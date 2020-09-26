//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import java.security.SecureRandom;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * This entity shares same primary key with the Book entity
 * which is simultaneously a foreign key (unidirectional mapping)
 * for this one.
 * For implementation reference, see also:
 * https://www.programmersought.com/article/1610322983/
 *
 * This entity generates a table which holds auto-generated
 * random string values associated to each book.
 *
 * These random string values represent difficult-to-enumerate
 * unique book IDs used for front-end purposes. Main purpose
 * is not to expose real Book entity id value.
 */

@Entity
public class BookHash {

	@Id
	@GeneratedValue(
			strategy   =  GenerationType.SEQUENCE,
			generator  = "bookHashIdGenerator"
			)
	@GenericGenerator(
			name       = "bookHashIdGenerator",
			strategy   = "foreign",
			parameters = { @Parameter(name = "property", value = "book") }
			)
	@Column(
			name       = "book_id",
			unique     = true,
			nullable   = false
			)
	@JsonIgnore
	private Long bookId;


	//@MapsId
	@OneToOne(
			cascade      = { CascadeType.MERGE, CascadeType.REMOVE },
			fetch        = FetchType.LAZY,
			mappedBy     = "bookHash",
			targetEntity = Book.class
			)
	@PrimaryKeyJoinColumn(
			referencedColumnName = "id"
			)
    private Book book;

	////////////////////
	// Attribute setters

	@Column(
			name             = "hash_id",
			unique           = true,
			columnDefinition = "CHAR(32)",
			updatable        = false,
			nullable         = false
			)
	private String hashId;


	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	/*
	 * This setter sets an auto-generated value
	 * No manual intervention.
	 */
	public void setHashId() {

		byte[] byteInit = new byte[16];

		new SecureRandom().nextBytes(byteInit);

		StringBuilder shaStringBuilder = new StringBuilder();

		for (byte b : byteInit) {
			shaStringBuilder.append(String.format("%02x", b));
		}

		this.hashId = shaStringBuilder.toString();

	}

	////////////////////
	// Attribute getters

	public Long getBookId() {
		return bookId;
	}

	public Book getBook() {
		return book;
	}

	public String getHashId() {
		return hashId;
	}

	////////////////////
	// Class constructors

	public BookHash() {
		this.setHashId();
	}

	////////////////////
	// Class overrides

	@Override
	public String toString() {
		return "[" + "book_id: " + this.bookId + ", " +
				"hash_id: " + this.hashId + "]";

	}

}